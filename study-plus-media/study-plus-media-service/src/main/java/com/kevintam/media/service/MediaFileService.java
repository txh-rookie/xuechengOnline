package com.kevintam.media.service;

import com.kevintam.media.model.dto.MediaFileDTO;
import com.kevintam.media.model.dto.QueryMediaParamsDto;
import com.kevintam.media.model.dto.UploadFileParamsDto;
import com.kevintam.media.model.po.MediaFiles;
import com.kevintam.study.entity.PageParams;
import com.kevintam.study.entity.PageResult;
import com.kevintam.study.entity.RestResponse;
import io.minio.errors.ServerException;
import org.springframework.web.multipart.MultipartFile;

public interface MediaFileService {

    PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

    /**
     * @param companyId 老师的id
     * @param uploadFileParamsDto 文件上传的参数
     * @param bytes 数据流
     * @param folder 文件夹
     * @param objectName 文件对象名
     * @return
     */
    //接口文件上传的通用接口，不经要上传视频，还要上传我们的文档、图片
    MediaFileDTO uploadCourseFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName);

    MediaFiles getMediaFileDTO(Long companyId, UploadFileParamsDto uploadFileParamsDto, String md5_file_id, String objectName,String bucketName);

    void uploadMinio(byte[] bytes, String objectName, String bucketName, String contentType) throws Exception;

    /**
     * 预览的功能，根据文件的id去查询
     * @param id
     * @return
     */
    RestResponse<String> preview(String id);
}
