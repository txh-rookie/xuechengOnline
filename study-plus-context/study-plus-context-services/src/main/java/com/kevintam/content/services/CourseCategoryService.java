package com.kevintam.content.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.content.mapper.CourseCategoryMapper;
import com.kevintam.dto.CourseCategoryDTO;
import com.kevintam.entity.CourseCategory;
import com.kevintam.study.entity.PageResult;

import java.util.List;

public interface CourseCategoryService {
    public List<CourseCategory> categoryTreeNodes();

    CourseCategory getCourseCategoryNodes(Long courseId);
}
