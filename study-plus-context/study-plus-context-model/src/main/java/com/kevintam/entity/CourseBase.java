package com.kevintam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/18
 * 课程基本信息
 */
@Data
@TableName("course_base")
public class CourseBase implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;//主键
    private Long companyId;//教师id
    private String companyName;//教师名称
    private String name;//课程名称
    private String users;//使用的人群,这里使用分割进行 学生_老师_研究生等
    private String tags;//课程的标签
    private String mt;//大的分类
    private String st;//小的分类
    private String grade;//课程的等级
    private String teachmode;//教育模式
    private String description;//课程的介绍
    private String pic;//图片
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;
    private String createPeople;//创建的人
    private String changePeople;//修改的人
    private String auditStatus;//审核状态
    private String status;//课程发布状态
}
