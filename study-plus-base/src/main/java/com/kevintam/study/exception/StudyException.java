package com.kevintam.study.exception;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/20
 * 自定义程序异常
 */
public class StudyException extends RuntimeException{
    private String message;
    public StudyException() {
    }

    @Override
    public String getMessage() {
        return message;
    }

    public StudyException(String message) {
        super(message);
        this.message=message;
    }

    public static void cast(String message){
       throw new StudyException(message);
    }
    public static void cast(CommonError commonError){
       throw new StudyException(commonError.getErrMessage());
    }
}
