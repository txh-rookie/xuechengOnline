package com.kevintam.content.api;

import com.kevintam.content.services.CourseTeacherService;
import com.kevintam.dto.AddCourseTeacherDTO;
import com.kevintam.dto.CourseTeacherDTO;
import com.kevintam.entity.CourseTeacher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/22
 */
@Slf4j
@RestController
@Api("教师接口")
public class CourseTeacherController {

    @Autowired
    private CourseTeacherService courseTeacherService;

//    http://localhost:8601/api/content/courseTeacher/list/117
    @GetMapping("/courseTeacher/list/{courseId}")
    @ApiOperation("教师接口的查询接口")
    public List<CourseTeacherDTO> courseTeacherDTOList(@PathVariable("courseId") Long courseId){
        return courseTeacherService.courseTeacherDTOList(courseId);
    }
    //更新操作
    //    http://localhost:8601/api/content/courseTeacher
    @PostMapping("/courseTeacher")
    @ApiOperation("教师信息添加接口")
    public void addCourseTeacher(@Validated() @RequestBody AddCourseTeacherDTO addCourseTeacherDTO){
        courseTeacherService.addCourseTeacherDTO(addCourseTeacherDTO);
    }
}
