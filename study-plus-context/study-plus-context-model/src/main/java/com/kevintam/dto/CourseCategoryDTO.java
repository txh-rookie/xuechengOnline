package com.kevintam.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.kevintam.entity.CourseCategory;
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
public class CourseCategoryDTO extends CourseCategory {
    private List<CourseCategory> childrenTreeNodes;//用于存储子节点
}
