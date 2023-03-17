package com.kevintam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/22
 */
@Data
public class AddCourseTeacherDTO {
//    {"courseId":22,"teacherName":"王老湿",
//            "position":"博士生导师","introduction":"博士生导师"}
    private Long id;
    @ApiModelProperty("课程信息id")
    @NotNull(message = "课程id不能为空")
    private Long courseId;
    @NotNull(message = "教师名称不能为空")
    @ApiModelProperty("教师名称")
    private String teacherName;
    @NotNull(message = "教师的职称不能为空")
    @ApiModelProperty("教师的职称")
    private String position;
    @ApiModelProperty("教师的的简介")
    private String introduction;
}
