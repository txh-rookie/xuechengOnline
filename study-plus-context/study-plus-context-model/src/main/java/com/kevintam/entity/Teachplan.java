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
 * @createDate 2023/2/20
 */
@Data
@TableName("teachplan")
public class Teachplan implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @ApiModelProperty("课程计划名称")
    private String pname;
    @ApiModelProperty("课程计划父级id")
    private Long parentid;
    @ApiModelProperty("层级")
    private Integer grade;
    @ApiModelProperty("课程类型")
    private String mediaType;
    @ApiModelProperty("开始直播时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束直播时间")
    private LocalDateTime endTime;
    @ApiModelProperty("章节介绍")
    private String description;
    @ApiModelProperty("时长")
    private String timelength;
    @ApiModelProperty("排序字段")
    private Integer orderby;
    @ApiModelProperty("课程标识")
    private Long courseId;
    @ApiModelProperty("课程发布标识")
    private Long coursePubId;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("是否支持试学")
    private Character isPreview;
    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;
    @ApiModelProperty("修改时间")
    private LocalDateTime changeDate;
}
