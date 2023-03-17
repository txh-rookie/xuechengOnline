package com.kevintam.content.services.impl;

import com.google.j2objc.annotations.AutoreleasePool;
import com.kevintam.content.mapper.CourseBaseMapper;
import com.kevintam.content.mapper.CoursePublishMapper;
import com.kevintam.content.mapper.CoursePublishPreMapper;
import com.kevintam.content.services.CourseBaseService;
import com.kevintam.content.services.CoursePublishService;
import com.kevintam.entity.CourseBase;
import com.kevintam.entity.CoursePublish;
import com.kevintam.entity.CoursePublishPre;
import com.kevintam.study.exception.StudyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/3/2
 */
@Slf4j
@Service
public class CoursePublishServiceImpl implements CoursePublishService {

    @Autowired
    private CoursePublishMapper coursePublishService;

    @Autowired
    private CoursePublishPreMapper coursePublishPreMapper;

    @Autowired
    private CoursePublishMapper coursePublishMapper;

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    

    /**
     * 课程发布
     *
     * @param courseId
     * @param companyId
     */
    @Override
    public void publish(Long courseId, Long companyId) {
        //约束校验
        //查询课程预发布表
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPre == null) {
            StudyException.cast("请先提交课程审核，审核通过才可以发布");
        }
        //本机构只允许提交本机构的课程
        if (!coursePublishPre.getCompanyId().equals(companyId)) {
            StudyException.cast("不允许提交其它机构的课程。");
        }

        //课程审核状态
        String auditStatus = coursePublishPre.getStatus();
        //审核通过方可发布
        if (!"202004".equals(auditStatus)) {
            StudyException.cast("操作失败，课程审核通过方可发布。");
        }
        //保存课程发布信息
        saveCoursePublish(courseId);
        //保存消息表
        saveCoursePublishMessage(courseId);
        //删除课程预发布表对应记录
        coursePublishPreMapper.deleteById(courseId);

    }

    /**
     * 向我们的发布表添加一条数据
     */
    private void saveCoursePublishMessage(Long courseId) {
        //整合课程发布信息
        //查询课程预发布表
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPre == null) {
            StudyException.cast("课程预发布数据为空");
        }
        CoursePublish coursePublish = new CoursePublish();
        //拷贝到课程发布对象
        BeanUtils.copyProperties(coursePublishPre, coursePublish);
        coursePublish.setStatus("203002");
        CoursePublish coursePublishUpdate = coursePublishMapper.selectById(courseId);
        if (coursePublishUpdate == null) {
            coursePublishMapper.insert(coursePublish);
        } else {
            coursePublishMapper.updateById(coursePublish);
        }
        //更新课程基本表的发布状态
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        courseBase.setStatus("203002");
        courseBaseMapper.updateById(courseBase);
    }

    /**
     * 从预信息表中
     * TODO: 2023/3/3  课程发布待续
     * @param courseId
     */
    private void saveCoursePublish(Long courseId) {
        /**
         * 课程发布的流程是:
         *   1、教师端通过我们的预览，课程相关信息，然后去发布课程信息到我们的预发布表，然后将状态变为审核状态
         *   2、我们的运营人员对这个课程信息进行审核，审核完成之后，会修改状态为预发布状态。
         *   3、教师端，看到我们的课程信息已经通过了，课程发布成功。状态会变为发布状态，就可以在前端看到该课程。
         *
         */
        CoursePublish coursePublish = coursePublishMapper.selectById(courseId);
        if(coursePublish==null){
            StudyException.cast("发布表中没有该数据");
        }

    }
}
