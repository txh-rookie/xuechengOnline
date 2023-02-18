package com.kevintam.vo;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/18
 */

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 封装查询到信息进行返回
 */
@Data
public class CourseSearchResultVo {
//    课程信息、任务数、创建时间、是否付费、审核状态、类型。
    private String courseName;//课程信息
    private Integer numberOfTasks;//任务数
    private LocalDateTime createDate;//创建时间
    private Integer chargeOrNot;//用一个状态码来表示是否付费。
    private String auditStatus;
    private String courseType;//是录播、直播、视频
}
