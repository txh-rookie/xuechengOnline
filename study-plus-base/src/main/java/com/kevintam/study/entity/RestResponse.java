package com.kevintam.study.entity;

import lombok.Data;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/25
 */
@Data
public class RestResponse<T> {
    private Integer code;
    private String message;
    private T result;

    public static RestResponse success(Boolean flag){
        RestResponse<Boolean> restResponse = new RestResponse<>();
        restResponse.setCode(0);
        restResponse.setResult(flag);
        return restResponse;
    }
    public static RestResponse error(Boolean flag){
        RestResponse<Boolean> restResponse = new RestResponse<>();
        restResponse.setCode(-1);
        restResponse.setResult(flag);
        return restResponse;
    }
}
