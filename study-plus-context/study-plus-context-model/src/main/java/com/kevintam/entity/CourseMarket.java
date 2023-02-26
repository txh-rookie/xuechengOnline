package com.kevintam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/19
 */
@Data
@TableName("course_market")
public class CourseMarket implements Serializable {
    private Long id;
    private String charge;
    private Float price;
    private Float originalPrice;
    private String qq;
    private String wechat;
    private String phone;
    private Integer validDays;
}
