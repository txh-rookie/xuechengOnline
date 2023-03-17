package com.kevintam.content.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevintam.content.mapper.*;
import com.kevintam.content.services.CourseAuditService;
import com.kevintam.content.services.CourseBaseService;
import com.kevintam.content.services.CourseTeacherService;
import com.kevintam.content.services.TeachplanService;
import com.kevintam.dto.CourseBaseInfoDto;
import com.kevintam.dto.TeachplanDTO;
import com.kevintam.entity.*;
import com.kevintam.study.exception.StudyException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/3/2
 */
@Service
public class CourseAuditServiceImpl implements CourseAuditService {

    @Autowired
    private CourseAuditMapper courseAuditMapper;

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private CoursePublishMapper coursePublishMapper;

    @Autowired
    private CourseTeacherMapper CourseTeacherMapper;

    @Autowired
    private CourseMarketMapper courseMarketMapper;
    @Autowired
    private TeachplanService teachplanService;

    @Autowired
    private CoursePublishPreMapper coursePublishPreMapper;

    @Transactional
    @Override
    public Boolean commitAudit(Long courseId, Long companyId) {
        //先去查询一下，这个要修改的数据
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            StudyException.cast("该课程不存在");
            return false;
        }
// 202002 表示未提交的 202001表示审核失败 202003 审核一提交 202004 审核成功
        String auditStatus = courseBase.getAuditStatus();
        if ("202003".equals(auditStatus)) {
            StudyException.cast("该课程以被审核");
            return false;
        }
        if (companyId == null && !companyId.equals(courseBase.getCompanyId())) {
            //表示不是该老师的课程不法进行提交
            StudyException.cast("该课程没有权限进行修改");
            return false;
        }
        QueryWrapper<CoursePublish> wrapper = new QueryWrapper<>();
        Integer publishCount = coursePublishMapper.selectCount(wrapper.eq("id", courseId).eq("company_id", companyId));
        if (publishCount > 0) {
            //表示该课程已在被审核过程中，无法提交
            StudyException.cast("该课程已在审核中，无法重覆提交");
            return false;
        }
        //课程图片是否填写
        if (StringUtils.isEmpty(courseBase.getPic())) {
            StudyException.cast("提交失败，请上传课程图片");
        }
        //将我们的课程拿到我们的预发布表中，进行查看
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        CourseBaseInfoDto courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);
        BeanUtils.copyProperties(courseBaseInfo, coursePublishPre);
        //拿到课程相关信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        if (courseMarket == null) {
            StudyException.cast("课程营销信息获取失败");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String market = null;
        try {
            market = objectMapper.writeValueAsString(courseMarket);
        } catch (JsonProcessingException e) {
            StudyException.cast("将课程营销信息转换成json失败");
            return false;
        }
        coursePublishPre.setMarket(market);
        List<TeachplanDTO> teachplanDTOS = teachplanService.selectTreeNodes(courseId);
        if (teachplanDTOS == null) {
            StudyException.cast("课程为null....");
            return false;
        }
        try {
            String teachplanDTOSJson = objectMapper.writeValueAsString(teachplanDTOS);
            coursePublishPre.setTeachplan(teachplanDTOSJson);
        } catch (JsonProcessingException e) {
            StudyException.cast("将课程计划服转换成json失败");
            return false;
        }
        //拿到老师信息
        CourseTeacher courseTeacher = CourseTeacherMapper.selectOne(new QueryWrapper<CourseTeacher>().eq("course_id", courseId));
        if (courseTeacher == null) {
            StudyException.cast("师资信息不能为null");
            return false;
        }
        try {
            String teacherJson = objectMapper.writeValueAsString(courseTeacher);
            coursePublishPre.setTeachers(teacherJson);
        } catch (JsonProcessingException e) {
            StudyException.cast("师资信息转换json失败");
            return false;
        }
        coursePublishPre.setStatus("202003");
        //封装完publishPre
        coursePublishPre.setAuditDate(LocalDateTime.now());
        CoursePublishPre coursePublishPre1 = coursePublishPreMapper.selectById(courseId);
        if(coursePublishPre1==null){
            int insert = coursePublishPreMapper.insert(coursePublishPre);
            if (insert <= 0) {
                StudyException.cast("存储预发布表信息失败");
            }
        }else{
            //进行更新
            coursePublishPreMapper.updateById(coursePublishPre1);
        }
        //修改courseBase中的审核信息
        CourseBase courseBase1 = new CourseBase();
        courseBase1.setAuditStatus("202003");
        courseBase1.setId(courseId);
        courseBaseMapper.updateById(courseBase1);
        return true;
    }
}
