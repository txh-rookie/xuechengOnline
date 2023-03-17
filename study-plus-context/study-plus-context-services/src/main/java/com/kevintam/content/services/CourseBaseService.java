package com.kevintam.content.services;

import com.kevintam.dto.AddCourseDto;
import com.kevintam.dto.CourseBaseInfoDto;
import com.kevintam.dto.SearchCourseNameDTO;
import com.kevintam.dto.UpdateCourseDTO;
import com.kevintam.study.entity.PageParams;
import com.kevintam.study.entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;

public interface CourseBaseService {
    public PageResult listCourse(PageParams pageParams,SearchCourseNameDTO params);

    /**
     * 新增课程信息
     * @param courseId 教师id、以及教师名
     * @param addCourseDto 新增的课程信息
     * @return
     */
    CourseBaseInfoDto addCourseInfo(Long courseId,AddCourseDto addCourseDto);

    CourseBaseInfoDto getCourseIdByCourseInfoDTO(Long courseId);

    CourseBaseInfoDto updateCourseInfoDTO(Long companyId,UpdateCourseDTO updateCourseDto);

    /**
     * 根据我们的课程id，去封装课程信息
     * @param courseId
     * @return
     */
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId);
}
