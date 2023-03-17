package com.kevintam.content.services;

import com.kevintam.dto.AddCourseTeacherDTO;
import com.kevintam.dto.CourseTeacherDTO;

import java.util.List;

public interface CourseTeacherService {
    List<CourseTeacherDTO> courseTeacherDTOList(Long courseId);

    void addCourseTeacherDTO(AddCourseTeacherDTO addCourseTeacherDTO);
}
