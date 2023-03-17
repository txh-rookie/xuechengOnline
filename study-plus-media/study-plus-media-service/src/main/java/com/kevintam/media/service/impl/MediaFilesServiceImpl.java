package com.kevintam.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.media.Utils.GetDateTool;
import com.kevintam.media.mapper.MediaFilesMapper;
import com.kevintam.media.mapper.MediaFilesProcessMapper;
import com.kevintam.media.model.dto.MediaFileDTO;
import com.kevintam.media.model.dto.QueryMediaParamsDto;
import com.kevintam.media.model.dto.UploadFileParamsDto;
import com.kevintam.media.model.po.MediaFiles;
import com.kevintam.media.model.po.MediaProcess;
import com.kevintam.media.service.MediaFileService;
import com.kevintam.study.entity.PageParams;
import com.kevintam.study.entity.PageResult;
import com.kevintam.study.entity.RestResponse;
import com.kevintam.study.exception.StudyException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/23
 */
@Slf4j
@Service
public class MediaFilesServiceImpl extends ServiceImpl<MediaFilesMapper, MediaFiles> implements MediaFileService {


    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MediaFilesMapper mediaFilesMapper;

    @Autowired
    private MediaFileService mediaFileService;

    @Autowired
    private MediaFilesProcessMapper mediaFilesProcessMapper;

    @Value("${minio.bucket.files}")
    private String filesBucket;
    @Value("${minio.bucket.video}")
    private String videoBucket;

    @Override
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
        //文件名 filename like "%#{filename}%" 查询
        queryWrapper.like(StringUtils.isNoneEmpty(queryMediaParamsDto.getFilename()), MediaFiles::getFilename, queryMediaParamsDto.getFilename());
        queryWrapper.eq(StringUtils.isNoneEmpty(queryMediaParamsDto.getAuditStatus()),
                MediaFiles::getAuditStatus, queryMediaParamsDto.getAuditStatus());

        queryWrapper.eq(StringUtils.isNoneEmpty(queryMediaParamsDto.getFileType()),
                MediaFiles::getFileType, queryMediaParamsDto.getFileType());
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Page<MediaFiles> mediaFilesPage = mediaFilesMapper.selectPage(page, queryWrapper);
        PageResult<MediaFiles> result = new PageResult<>(mediaFilesPage.getRecords(), mediaFilesPage.getTotal(), mediaFilesPage.getCurrent(), mediaFilesPage.getSize());
        return result;
    }

    /**
     * 具体实现
     *
     * @param companyId           老师的id
     * @param uploadFileParamsDto 文件上传的参数
     * @param bytes               数据流
     * @param folder              文件夹
     * @param objectName          文件对象名
     * @return
     */
    @Override
    public MediaFileDTO uploadCourseFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName) {
        //根据文件的大小生成md5
        String md5_file_id = DigestUtils.md5Hex(bytes);
        //1、folder为null的话，需要默认的文件路径
        if (StringUtils.isEmpty(folder)) {
            //如果传入的文件夹为空，需要默认的文件夹
            folder = GetDateTool.getFileFolder(new Date(), true, true, true);
        } else if (folder.indexOf("/") < 0) {
            //有值
            folder += "/";
        }
        //文件名为null
        if (StringUtils.isEmpty(objectName)) {
            String filename = uploadFileParamsDto.getFilename();
            if (filename != null) {
                //md5.文件的后缀
                objectName = md5_file_id + filename.substring(filename.lastIndexOf("."));
            } else {
                StudyException.cast("该参数不能为空");
            }
        }
        objectName = folder + objectName;

        //文件上传
        try {
            uploadMinio(bytes, objectName, filesBucket, uploadFileParamsDto.getContentType());

            //取用这个id去查一下我们数据库中是否存在值
            MediaFiles mediaFiles = mediaFileService.getMediaFileDTO(companyId, uploadFileParamsDto, md5_file_id, objectName, filesBucket);
            MediaFileDTO mediaFileDTO = new MediaFileDTO();
            BeanUtils.copyProperties(mediaFiles, mediaFileDTO);
            return mediaFileDTO;
        } catch (Exception e) {
            log.error("文件上传失败:{}", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    //文件上传的方法
    public void uploadMinio(byte[] bytes, String objectName, String filesBucket, String contentType) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        //minio上传文件
        PutObjectArgs build = PutObjectArgs.builder()
                .bucket(filesBucket)
                .object(objectName)
                /**
                 * InputStream stream,  需要传入一个输入流
                 * long objectSize,流的大小
                 * long partSize:分区的大小 -1 5m 最大不要超过5T
                 */
                .stream(byteArrayInputStream, byteArrayInputStream.available(), -1)
                //上传类型
                .contentType(contentType)
                .build();

        minioClient.putObject(build);
    }

    @Override
    public RestResponse<String> preview(String id) {
        if (!StringUtils.isNotEmpty(id)) {
            StudyException.cast("预览功能必须上传文件的id");
        }
        MediaFiles mediaFiles = mediaFilesMapper.selectById(id);
        if (mediaFiles == null) {
            StudyException.cast("文件id错误...");
        }
        String url = mediaFiles.getUrl();
        //处理一下url，如果是mp4、png、mp3等都可以生成url返回给前端
        RestResponse<String> stringRestResponse = new RestResponse<>();
        if (url.indexOf("mp4") >= 0 ||
                url.indexOf("png") >= 0 ||
                url.indexOf("mp3") >= 0 || url.indexOf("jpg") >= 0) {
            stringRestResponse.setResult(url);
            stringRestResponse.setCode(0);
        } else {
            stringRestResponse.setCode(-1);
            stringRestResponse.setResult(null);
        }
        return stringRestResponse;
    }

    //将mediaFileDTO进行封装,上传到数据库
    @Transactional
    public MediaFiles getMediaFileDTO(Long companyId, UploadFileParamsDto uploadFileParamsDto, String md5_file_id, String objectName, String bucketName) {
        MediaFiles mediaFiles = this.getById(md5_file_id);
        //数据库中没有查询到的
        if (mediaFiles == null) {
            //数据库中没有该文件,进行插入操作
            mediaFiles = new MediaFiles();
            //拷贝基本信息
            BeanUtils.copyProperties(uploadFileParamsDto, mediaFiles);
            mediaFiles.setId(md5_file_id);
            mediaFiles.setFileId(md5_file_id);
            mediaFiles.setCompanyId(companyId);
            mediaFiles.setUrl("/" + bucketName + "/" + objectName);
            mediaFiles.setBucket(bucketName);
            mediaFiles.setFilePath(objectName);
            mediaFiles.setCreateDate(LocalDateTime.now());
            mediaFiles.setStatus("1");
            //将avi的视频上传到待处理的表中
            if (objectName.lastIndexOf(".avi") >= 0) {
                MediaProcess mediaProcess = new MediaProcess();
                mediaProcess.setFileId(md5_file_id);
                mediaProcess.setFilename(uploadFileParamsDto.getFilename());
                mediaProcess.setBucket(bucketName);
                mediaProcess.setFilePath(objectName);
                mediaProcess.setStatus("1");
                mediaProcess.setCreateDate(LocalDateTime.now());
                mediaProcess.setUrl("/" + bucketName + "/" + objectName);
                mediaFilesProcessMapper.insert(mediaProcess);
            }
            boolean insert = this.save(mediaFiles);
            if (!insert) {
                StudyException.cast("保存文件信息失败");
            }
            return mediaFiles;
        }
        //查询到数据，不做处理
        return mediaFiles;
    }
}
