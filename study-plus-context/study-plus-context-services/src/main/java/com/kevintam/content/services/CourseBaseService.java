package com.kevintam.content.services;

import com.kevintam.dto.SearchCourseNameDTO;
import com.kevintam.study.entity.PageParams;
import com.kevintam.study.entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;

public interface CourseBaseService {
    public PageResult listCourse(PageParams pageParams,SearchCourseNameDTO params);
}
