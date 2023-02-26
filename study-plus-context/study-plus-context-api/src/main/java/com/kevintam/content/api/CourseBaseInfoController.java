package com.kevintam.content.api;

import com.kevintam.content.services.CourseBaseService;
import com.kevintam.content.services.CourseCategoryService;
import com.kevintam.dto.*;
import com.kevintam.entity.CourseBase;
import com.kevintam.entity.CourseCategory;
import com.kevintam.study.Volidate.ValidationGroups;
import com.kevintam.study.entity.PageParams;
import com.kevintam.study.entity.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    @Resource
    private CourseCategoryService categoryService;

    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> listCourseBase(PageParams pageParams, @RequestBody SearchCourseNameDTO params) {
        return courseBaseService.listCourse(pageParams, params);
    }

    @ApiOperation("获取课程分类的接口")
    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategory> categoryNodes() {
        return categoryService.categoryTreeNodes();
    }

    /**
     * 用于接收前端传入的信息
     * @param addCourseDto
     * @return
     * @Validate 相当于开启来jsr303数据校验的规则
     */
    @ApiOperation("添加课程信息的接口")
    @PostMapping("/course")
    public CourseBaseInfoDto addCourseInfo(@RequestBody @Validated() AddCourseDto addCourseDto){
//        return courseBaseService.addCourseInfo(addCourseDto.get,addCourseDto);
        // TODO: 2023/2/20  id我们后面写权限的审核，在来写
        Long compareId=22L;
        return courseBaseService.addCourseInfo(compareId,addCourseDto);
    }

//    content/course/28 get
    @ApiOperation("根据课程id查询课程信息")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseIdByCourseInfoDTO(@PathVariable("courseId") Long courseId){
         return courseBaseService.getCourseIdByCourseInfoDTO(courseId);
    }

    @ApiOperation("更新课程信息的接口")
    @PutMapping("/course")
    public CourseBaseInfoDto updateCourseInfoDTO(@RequestBody @Validated UpdateCourseDTO updateCourseDTO){
        Long companyId=22L;
        return courseBaseService.updateCourseInfoDTO(companyId,updateCourseDTO);
    }
}
