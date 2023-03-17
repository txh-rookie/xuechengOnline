package com.kevintam.content.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kevintam.content.mapper.CourseTeacherMapper;
import com.kevintam.content.services.CourseTeacherService;
import com.kevintam.dto.AddCourseTeacherDTO;
import com.kevintam.dto.CourseTeacherDTO;
import com.kevintam.entity.CourseTeacher;
import com.kevintam.study.exception.StudyException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/22
 */
@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<CourseTeacherDTO> courseTeacherDTOList(Long courseId) {
        if(courseId==null){
            StudyException.cast("参数不能为null");
        }
        QueryWrapper<CourseTeacher> courseTeacherDTOQueryWrapper = new QueryWrapper<>();
        List<CourseTeacher> courseTeacherList= courseTeacherMapper.selectList(courseTeacherDTOQueryWrapper.eq("course_id", courseId));
        if(courseTeacherList.isEmpty()){
            StudyException.cast("系统没有该老师");
        }
        List<CourseTeacherDTO> resultList=new ArrayList<>();
        for (CourseTeacher courseTeacher : courseTeacherList) {
            CourseTeacherDTO courseTeacherDTO = new CourseTeacherDTO();
            BeanUtils.copyProperties(courseTeacher,courseTeacherDTO);
            resultList.add(courseTeacherDTO);
        }
        return resultList;
    }

    @Override
    public void addCourseTeacherDTO(AddCourseTeacherDTO addCourseTeacherDTO) {
        if(addCourseTeacherDTO.getCourseId()==null){
            StudyException.cast("参数类型不正确...");
        }
        if(addCourseTeacherDTO.getId()==null){
            CourseTeacher courseTeacher = new CourseTeacher();
            BeanUtils.copyProperties(addCourseTeacherDTO,courseTeacher);
            courseTeacherMapper.insert(courseTeacher);
        }else{
            CourseTeacher courseTeacher = new CourseTeacher();
            BeanUtils.copyProperties(addCourseTeacherDTO,courseTeacher);
            courseTeacherMapper.updateById(courseTeacher);
        }
    }
}
