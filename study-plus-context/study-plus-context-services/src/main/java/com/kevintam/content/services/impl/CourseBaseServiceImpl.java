package com.kevintam.content.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.content.mapper.CourseBaseMapper;
import com.kevintam.content.services.CourseBaseService;
import com.kevintam.dto.SearchCourseNameDTO;
import com.kevintam.entity.CourseBase;
import com.kevintam.study.entity.PageParams;
import com.kevintam.study.entity.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/18
 */
@Service
@Slf4j
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    /**
     * 根据参数进行查询
     */
    @Override
    public PageResult listCourse(PageParams pageParams, SearchCourseNameDTO params) {

        //1、根据查询条件查询数据库
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<CourseBase>();
        //根据name进行模糊查询 like "%华为%",封装的模糊查询
        queryWrapper.like(StringUtils.isNoneEmpty(params.getCourseName()),CourseBase::getName,params.getCourseName());
        //根据审核状态进行查询 第一个参数是是否存在、第二参数是要查询的数据、第三个是具体的数据

        queryWrapper.eq(StringUtils.isNoneEmpty(params.getAuditStatus()),
                CourseBase::getAuditStatus,params.getAuditStatus());

        queryWrapper.eq(StringUtils.isNoneEmpty(params.getSubmitStatus()),
                CourseBase::getStatus,params.getSubmitStatus());
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        //查询数据库 select * from courseBase <where> <if test="name!=null">name=#{name}</where>xxxx
        Page<CourseBase> courseBasePage = courseBaseMapper.selectPage(page, queryWrapper);
        PageResult<CourseBase> pageResult = new PageResult<>(courseBasePage.getRecords(),courseBasePage.getTotal()
                , pageParams.getPageNo(), pageParams.getPageSize());
        return pageResult;
    }
}
