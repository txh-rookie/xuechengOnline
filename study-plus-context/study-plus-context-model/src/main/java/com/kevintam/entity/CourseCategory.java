package com.kevintam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/19
 */
@Data
@TableName(value = "course_category")
public class CourseCategory {
    @TableId(value = "id")
    private String id;
    private String name;
    private String label;
    private String parentid;
    private Integer isShow;
    private String orderby;//排序
    private Integer isLeaf;
    @TableField(exist = false)
    private List<CourseCategory> childrenTreeNodes;//用于存储子节点
}
