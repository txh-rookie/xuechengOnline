package com.kevintam.dto;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/18
 */

import lombok.Data;

/**
 * Dto数据传输的相关的模型
 * 封装的前端的条件查询的实体类
 */
@Data
public class SearchCourseNameDTO{
     private String CourseName;//根据课程名进行查询
     private String auditStatus;//根据审核状态来进行过滤
     private String submitStatus;//根据提交状态来进行过滤
}
