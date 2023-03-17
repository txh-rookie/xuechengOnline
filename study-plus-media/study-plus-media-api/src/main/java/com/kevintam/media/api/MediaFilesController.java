package com.kevintam.media.api;

import com.kevintam.media.model.dto.MediaFileDTO;
import com.kevintam.media.model.dto.QueryMediaParamsDto;
import com.kevintam.media.model.dto.UploadFileParamsDto;
import com.kevintam.media.model.po.MediaFiles;
import com.kevintam.media.service.MediaFileService;
import com.kevintam.study.entity.PageParams;
import com.kevintam.study.entity.PageResult;
import com.kevintam.study.entity.RestResponse;
import com.kevintam.study.exception.StudyException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/23
 */
@RestController
@Api("媒资管理接口")
public class MediaFilesController {
    @Autowired
    MediaFileService mediaFileService;


    @ApiOperation("媒资列表查询接口")
    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams pageParams, @RequestBody QueryMediaParamsDto queryMediaParamsDto) {
        Long companyId = 1232141425L;
        return mediaFileService.queryMediaFiles(companyId, pageParams, queryMediaParamsDto);
    }

    //    http://localhost:8601/api/media/upload/coursefile
    @ApiOperation("媒资管理的上传资源的接口")
    @RequestMapping(value = "/upload/coursefile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public MediaFileDTO uploadCourseFile(@RequestPart("filedata") MultipartFile upload,
                                         @RequestParam(value = "folder", required = false) String folder,
                                         @RequestParam(value = "objectName", required = false) String objectName) {
        Long companyId = 1232141425L;
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        String contentType = upload.getContentType();
        uploadFileParamsDto.setContentType(contentType);
        uploadFileParamsDto.setFileSize(upload.getSize());
//        如果这个类型中出现image，表示是图片类型
        if (contentType.indexOf("image") >= 0) {
            //表示是图片
            uploadFileParamsDto.setFileType("001001");
        } else {
            uploadFileParamsDto.setFilename("001003");
        }
        //拿到原始名称
        uploadFileParamsDto.setFilename(upload.getOriginalFilename());
        MediaFileDTO mediaFileDTO = null;
        try {
            mediaFileDTO = mediaFileService.uploadCourseFile(companyId, uploadFileParamsDto, upload.getBytes(), folder, objectName);
        } catch (IOException e) {
            StudyException.cast("上传文件出错");
        }
        return mediaFileDTO;
    }
//    http://localhost:8601/api/media
//    /preview/03eeb4bdb2a389d8a7fb26becc3891ad
    @ApiOperation("预览的接口")
    @GetMapping("/preview/{id}")
    public RestResponse<String>  preview(@PathVariable("id") String id){
        return  mediaFileService.preview(id);
    }
}
