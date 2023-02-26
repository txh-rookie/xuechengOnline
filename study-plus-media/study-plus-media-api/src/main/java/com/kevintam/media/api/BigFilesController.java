package com.kevintam.media.api;

import com.kevintam.media.model.dto.MediaFileDTO;
import com.kevintam.media.model.dto.UploadFileParamsDto;
import com.kevintam.media.service.BigUploadFileService;
import com.kevintam.study.entity.RestResponse;
import com.kevintam.study.exception.StudyException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/25
 */
@RestController
@Api("大文件上传的接口")
public class BigFilesController {

    @Autowired
    private BigUploadFileService bigUploadFileService;

    @ApiOperation(value = "文件上传前检查文件")
    @PostMapping("/upload/checkfile")
    public RestResponse<Boolean> checkfile(@RequestParam("fileMd5") String fileMd5) throws Exception {

        return bigUploadFileService.checkFile(fileMd5);
    }

    @ApiOperation(value = "分块文件上传前的检测")
    @PostMapping("/upload/checkchunk")
    public RestResponse<Boolean> checkchunk(@RequestParam("fileMd5") String fileMd5,
                                            @RequestParam("chunk") int chunk) throws Exception {
        return bigUploadFileService.checkChunk(fileMd5, chunk);
    }

    @ApiOperation(value = "上传分块文件")
    @PostMapping("/upload/uploadchunk")
    public RestResponse uploadchunk(@RequestParam("file") MultipartFile file,
                                    @RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("chunk") int chunk) throws Exception {
        return bigUploadFileService.uploadChunk(file.getBytes(), fileMd5, chunk, file.getContentType());

    }

    @ApiOperation(value = "合并文件")
    @PostMapping("/upload/mergechunks")
    public RestResponse mergechunks(@RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("chunkTotal") int chunkTotal) throws Exception {
//        return bigUploadFileService.mergeChunks(fileMd5,fileName,chunkTotal);
        Long companyId = 1232141425L;
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFilename(fileName);
        uploadFileParamsDto.setFileType("001002");
       return bigUploadFileService.mergeChunks(companyId,fileMd5,chunkTotal,uploadFileParamsDto);
    }
}
