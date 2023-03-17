package com.kevintam.study.exception;

import java.io.Serializable;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/20
 * 定义异常的返回结果
 */
public class RestErrorResponse implements Serializable {
    private String errMessage;
    public RestErrorResponse(String errMessage){
        this.errMessage= errMessage;
    }
    public String getErrMessage() {
        return errMessage;
    }
    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
