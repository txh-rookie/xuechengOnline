package com.kevintam.content.api;

import com.kevintam.content.services.CourseBaseService;
import com.kevintam.dto.SearchCourseNameDTO;
import com.kevintam.entity.CourseBase;
import com.kevintam.study.entity.PageParams;
import com.kevintam.study.entity.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/18
 */
@Api(value = "课程信息编辑接口", tags = "课程信息编辑接口")
@RestController
public class CourseBaseInfoController {

    @Resource
    private CourseBaseService courseBaseService;

    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> listCourseBase(@ApiParam("分页参数") PageParams pageParams, @RequestBody SearchCourseNameDTO params) {
        return courseBaseService.listCourse(pageParams,params);
    }
}
