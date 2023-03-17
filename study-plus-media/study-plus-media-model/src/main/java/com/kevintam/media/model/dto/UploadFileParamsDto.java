package com.kevintam.media.model.dto;

import lombok.Data;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/23
 * 文件上传接口的通用接口
 */
@Data
public class UploadFileParamsDto {

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件content-type
     */
    private String contentType;

    /**
     * 文件类型（文档，音频，视频）
     */
    private String fileType;
    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 标签
     */
    private String tags;

    /**
     * 上传人
     */
    private String username;

    /**
     * 备注
     */
    private String remark;

}
