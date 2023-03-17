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
 * @createDate 2023/3/2
 */
@Data
@TableName("course_audit")
public class CourseAudit implements Serializable {

    private static final long serialVersionUID = -4714996857685038731L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("课程id")
    private Long courseId;
    @ApiModelProperty("审核意见")
    private String auditMind;
    @ApiModelProperty("审核状态")
    private String auditStatus;
    @ApiModelProperty("审核人")
    private String auditPeople;
    @ApiModelProperty("审核时间")
    private LocalDateTime dateTime;
}
