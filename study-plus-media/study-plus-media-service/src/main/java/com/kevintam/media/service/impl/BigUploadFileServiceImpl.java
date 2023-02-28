package com.kevintam.media.service.impl;

import com.kevintam.media.mapper.MediaFilesMapper;
import com.kevintam.media.model.dto.UploadFileParamsDto;
import com.kevintam.media.model.po.MediaFiles;
import com.kevintam.media.service.BigUploadFileService;
import com.kevintam.media.service.MediaFileService;
import com.kevintam.study.entity.RestResponse;
import com.kevintam.study.exception.StudyException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/25
 */
@Service
@Slf4j
public class BigUploadFileServiceImpl implements BigUploadFileService {
    @Autowired
    private MediaFilesMapper mediaFilesMapper;

    @Autowired
    private MediaFileService mediaFileService;

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.video}")
    private String bucketName;


    @Override
    public RestResponse<Boolean> checkFile(String fileMd5) {
        if (StringUtils.isEmpty(fileMd5)) {
            StudyException.cast("参数类型错误.....");
        }
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        InputStream stream = null;
        if (mediaFiles != null) {
            //表示数据库中存在该文件,需要从minio去查询一下是否存在。
            String bucket = mediaFiles.getBucket();//拿到桶
            String filePath = mediaFiles.getFilePath();
            GetObjectArgs getObjectArgs = GetObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(filePath)
                    .build();
            try {
                stream = minioClient.getObject(getObjectArgs);
                if (stream != null) {
                    //表示文件存在
                    RestResponse.success(true);
                }
            } catch (Exception e) {
                StudyException.cast(e.getMessage());
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return RestResponse.success(false);
    }

    //检查分块情况和md5
    @Override
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunk) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        //获取分块分拣所有的目录
        String fileFolderPath = getChunkFileFolderPath(fileMd5);
        //获取分块文件所在的位置
        String chunkFileFolder = fileFolderPath + chunk;
        GetObjectArgs build = GetObjectArgs.builder().bucket(bucketName).object(chunkFileFolder).build();
        try {
            InputStream stream = minioClient.getObject(build);
            if (stream == null) {
                return RestResponse.success(false);
            }
        } catch (Exception e) {
            return RestResponse.success(false);
        }
        return RestResponse.success(true);
    }

    /**
     * 文件上传
     *
     * @param bytes       文件的字节
     * @param fileMd5     文件的名称
     * @param chunk       当前分块
     * @param contentType
     * @return
     */
    @Override
    public RestResponse uploadChunk(byte[] bytes, String fileMd5, int chunk, String contentType) {
        //拼接上参数,拿到当前文件的对象
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //当前对象
        String uploadFileChunk = chunkFileFolderPath + chunk;

        try {
            mediaFileService.uploadMinio(bytes, uploadFileChunk, bucketName, contentType);
        } catch (Exception e) {
            return RestResponse.success(false);
        }
        return RestResponse.success(true);
    }

    /**
     * 合并文件的方法
     *
     * @param companyId           教师id
     * @param fileMd5             md5 id
     * @param chunkTotal          分块总数
     * @param uploadFileParamsDto 文件上传的参数
     * @return
     */
    @Override
    @Transactional
    public RestResponse mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
        //1、下载分块文件
        File[] downFiles = downloadBlockFile(fileMd5, chunkTotal);
        //拿到文件的后缀
        String suffix = uploadFileParamsDto.getFilename().substring(uploadFileParamsDto.getFilename().lastIndexOf("."));
        //1、合并的file对象
        File merge = null;
        try {
            //创建一个用于合并的临时对象
            merge = File.createTempFile(fileMd5, suffix);
        } catch (IOException e) {
            StudyException.cast("创建临时文件出错....");
        }
        RandomAccessFile mergeAccessFile = null;
        RandomAccessFile randomAccessReader = null;
        try {
            //用于将读取来的文件进行合并
            mergeAccessFile = new RandomAccessFile(merge, "rw");
            for (File downFile : downFiles) {
                //创建一个流对象，用于读取下载下来的分块文件
                randomAccessReader = new RandomAccessFile(downFile, "r");
                int len = -1;
                byte[] bytes = new byte[1024];
                //一边读，一边写
                while ((len = randomAccessReader.read(bytes)) != -1) {
                    mergeAccessFile.write(bytes, 0, len);
                }
            }
        } catch (Exception e) {
            StudyException.cast("合并文件出错......");
        } finally {
            if (mergeAccessFile != null) {
                try {
                    mergeAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (randomAccessReader != null) {
                try {
                    randomAccessReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //合并完成之后，进行验证
        try {
            InputStream fileInputStream = new FileInputStream(merge);
            String mergeMd5 = DigestUtils.md5Hex(fileInputStream);
            if (!mergeMd5.equals(fileMd5)) {
                log.error("文件校验失败");
                StudyException.cast("文件校验失败");
            }
            //3、将合并好的文件进行上传
            //拿到我们的存储路径
            String mergeFilePath = getChunkFileFolderPath(fileMd5, suffix);
            uploadBigFiles(merge.getAbsolutePath(), bucketName, mergeFilePath);
            //4、将视频信息，上传到数据库
            mediaFileService.getMediaFileDTO(companyId, uploadFileParamsDto, fileMd5, mergeFilePath, bucketName);
        } catch (IOException e) {
            log.error("文件上传出错:" + e);
        } finally {
            //删除临时文件
            if (downFiles != null) {
                for (File downFile : downFiles) {
                    if (downFile.exists()) {
                        downFile.delete();
                    }
                }
            }
            if (merge != null) {
                if (merge.exists()) {
                    merge.delete();
                }
            }
        }
        return RestResponse.success(true);
    }

    public void uploadBigFiles(String filePath, String bucketName, String fileName) {
        try {
            UploadObjectArgs build = UploadObjectArgs.builder().bucket(bucketName).filename(filePath).object(fileName).build();
            minioClient.uploadObject(build);
            log.debug("文件上传成功:{}", filePath);
        } catch (Exception e) {
            log.error("文件上传失败:{}", filePath);
        }
    }

    /**
     * 根据fileMd5下载到本地到方法中
     *
     * @param fileMd5
     * @param chunkTotal
     * @return
     */
    private File[] downloadBlockFile(String fileMd5, int chunkTotal) {
        //拿到minio中的文件所在的目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //进行循环下载
        //准备一个输入流
        FilterInputStream stream = null;
        //准备一个输出流
        FileOutputStream outputStream = null;
        //准备返回的所有下载下来的文件对象
        File[] files = new File[chunkTotal];
        //根据我们的分块文件进行循环，比如说我们有28个文件，那么就去下28次将我们的文件全部下载下来。
        for (int i = 0; i < chunkTotal; i++) {
            File tempFile = null;
            try {
                tempFile = File.createTempFile("chunk", null);
            } catch (IOException e) {
                StudyException.cast("文件创建不成功");
            }
            //对应在我们的minio中的分块文件
            String chunkFilePath = chunkFileFolderPath + i;
            try {
                //拿到文件的字节输入流
                GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(chunkFilePath)
                        .build();
                stream = minioClient.getObject(getObjectArgs);
                //创建文件，然后往文件中写入
                outputStream = new FileOutputStream(tempFile);
                IOUtils.copy(stream, outputStream);
                files[i] = tempFile;
            } catch (Exception e) {
                StudyException.cast("分块文件出错");
            } finally {
                if (stream != null && outputStream != null) {
                    try {
                        stream.close();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return files;
    }

    /**
     * 定义分块文件存在的目录
     */
    private String getChunkFileFolderPath(String md5File) {
        return md5File.substring(0, 1) + "/" + md5File.substring(1, 2) + "/" + md5File + "/" + "chunk" + "/";
    }

    private String getChunkFileFolderPath(String md5File, String extension) {
        return md5File.substring(0, 1) + "/" + md5File.substring(1, 2) + "/" + md5File + "/" + md5File  + extension;
    }
}
