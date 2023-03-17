package com.kevintam.dto;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/21
 */

import lombok.Data;

import java.io.Serializable;

/**
 * 用于添加的课程计划
 * 可以是新增章节、也可以是小节
 */
@Data
public class AddTeachplanDTO implements Serializable {

    private static final long serialVersionUID = -1293094380466526167L;
    //课程计划的id
    private Long id;
    /**
     * 课程计划名称 */
    private String pname;
    /**
     * 课程计划父级Id */
    private Long parentid;
    /**
     * 层级，分为1、2、3级 */
    private Integer grade;
    /**
     * 课程类型:1视频、2文档 */
    private String mediaType;
    /**
     * 课程标识 */
    private Long courseId;
    /**
     * 课程发布标识 */
    private Long coursePubId;
    /**
     * 是否支持试学或预览(试看) */
    private String isPreview;
}
