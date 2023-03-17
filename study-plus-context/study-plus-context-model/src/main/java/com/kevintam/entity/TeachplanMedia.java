package com.kevintam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/20
 */
@Data
@TableName("teachplan_media")
public class TeachplanMedia {
    @TableId(value = "id")
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("媒资文件id")
    private String mediaId;
    @ApiModelProperty("课程计划标识")
    private Long teachplanId;
    @ApiModelProperty("课程标识")
    private Long courseId;
    @ApiModelProperty("媒资文件原始名称")
    private String mediaFilename;
    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;
    @ApiModelProperty("创建人")
    private String createPeople;
    @ApiModelProperty("修改人")
    private String changePeople;
}
