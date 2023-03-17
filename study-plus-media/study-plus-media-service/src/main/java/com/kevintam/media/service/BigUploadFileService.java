package com.kevintam.media.service;

import com.kevintam.media.model.dto.UploadFileParamsDto;
import com.kevintam.study.entity.RestResponse;

public interface BigUploadFileService {
    RestResponse<Boolean> checkFile(String fileMd5);

    RestResponse<Boolean> checkChunk(String fileMd5, int chunk);

    RestResponse uploadChunk(byte[] bytes, String fileMd5, int chunk, String contentType);

//     mergeChunks(String fileMd5, String fileNRestResponseame, int chunkTotal);
    RestResponse mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto);
    public void uploadBigFiles(String filePath, String bucketName, String fileName);
}
