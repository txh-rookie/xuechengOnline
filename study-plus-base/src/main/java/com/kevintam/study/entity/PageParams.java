package com.kevintam.study.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/18
 */
@Data
public class PageParams {
    //当前页码默认值
    public static final long DEFAULT_PAGE_CURRENT = 1L;
    //每页记录数默认值
    public static final long DEFAULT_PAGE_SIZE = 10L;
    //当前页码
    @ApiModelProperty(value = "当前页码",example = "1")
    private Long pageNo = DEFAULT_PAGE_CURRENT;
    //每页记录数默认值
    @ApiModelProperty(value = "每页记录数",example = "10")
    private Long pageSize = DEFAULT_PAGE_SIZE;
    public PageParams(){
    }
    public PageParams(long pageNo,long pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
