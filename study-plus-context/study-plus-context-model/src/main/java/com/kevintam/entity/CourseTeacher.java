package com.kevintam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/22
 */
@Data
@TableName("course_teacher")
public class CourseTeacher implements Serializable {

    private static final long serialVersionUID = 6324106661919828580L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("教师标识")
    private Long courseId;
    @ApiModelProperty("教师信息")
    private String teacherName;
    @ApiModelProperty("教师职位")
    private String position;
    @ApiModelProperty("教师简介")
    private String introduction;
    @ApiModelProperty("照片")
    private String photograph;
    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;
}
