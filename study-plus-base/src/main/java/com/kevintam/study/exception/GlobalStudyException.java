package com.kevintam.study.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/20
 * 全局异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalStudyException {

    @ResponseBody
    @ExceptionHandler(StudyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse restErrorResponse(StudyException e){
        log.error("系统异常信息",e.getMessage(),e);
        return new RestErrorResponse(e.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse systemResponse(Exception e){
        log.error("系统异常信息",e.getMessage(),e);
        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse JSR303Exception(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();//拿到一个返回异常信息
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();//拿到所有的异常信息
        StringBuilder errorMessage=new StringBuilder();
        fieldErrors.forEach(message->{
            errorMessage.append(message.getDefaultMessage()).append(",");
        });
        log.error("系统异常信息",errorMessage.toString());
        return new RestErrorResponse(errorMessage.toString());
    }

}
