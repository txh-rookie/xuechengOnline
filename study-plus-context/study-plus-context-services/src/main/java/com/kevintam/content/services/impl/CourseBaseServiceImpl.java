package com.kevintam.content.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.content.mapper.CourseBaseMapper;
import com.kevintam.content.mapper.CourseCategoryMapper;
import com.kevintam.content.mapper.CourseMarketMapper;
import com.kevintam.content.services.CourseBaseService;
import com.kevintam.content.services.CourseCategoryService;
import com.kevintam.dto.AddCourseDto;
import com.kevintam.dto.CourseBaseInfoDto;
import com.kevintam.dto.SearchCourseNameDTO;
import com.kevintam.dto.UpdateCourseDTO;
import com.kevintam.entity.CourseBase;
import com.kevintam.entity.CourseCategory;
import com.kevintam.entity.CourseMarket;
import com.kevintam.study.entity.PageParams;
import com.kevintam.study.entity.PageResult;
import com.kevintam.study.exception.CommonError;
import com.kevintam.study.exception.StudyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CourseCategoryMapper categoryMapper;

    /**
     * 根据参数进行查询
     */
    @Override
    public PageResult listCourse(PageParams pageParams, SearchCourseNameDTO params) {

        //1、根据查询条件查询数据库
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<CourseBase>();
        //根据name进行模糊查询 like "%华为%",封装的模糊查询
        queryWrapper.like(StringUtils.isNoneEmpty(params.getCourseName()), CourseBase::getName, params.getCourseName());
        //根据审核状态进行查询 第一个参数是是否存在、第二参数是要查询的数据、第三个是具体的数据

        queryWrapper.eq(StringUtils.isNoneEmpty(params.getAuditStatus()),
                CourseBase::getAuditStatus, params.getAuditStatus());

        queryWrapper.eq(StringUtils.isNoneEmpty(params.getSubmitStatus()),
                CourseBase::getStatus, params.getSubmitStatus());
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        //查询数据库 select * from courseBase <where> <if test="name!=null">name=#{name}</where>xxxx
        Page<CourseBase> courseBasePage = courseBaseMapper.selectPage(page, queryWrapper);
        PageResult<CourseBase> pageResult = new PageResult<>(courseBasePage.getRecords(), courseBasePage.getTotal()
                , pageParams.getPageNo(), pageParams.getPageSize());
        return pageResult;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto addCourseInfo(Long courseId, AddCourseDto addCourseDto) {
        if (addCourseDto == null) {
            return null;
        }
        //封装信息
        // TODO: 2023/2/20  差一个教师名称，等后续写完权限系统的时候，在回来进行封装
        CourseBase courseBase = new CourseBase();
        courseBase.setCompanyId(courseId);
        //设置一下创建时间
        courseBase.setCreateDate(LocalDateTime.now());
        //设置一下发布状体 未发布
        courseBase.setStatus("203001");
        //设置一下审核状态 未审核
        courseBase.setAuditStatus("202002");
        BeanUtils.copyProperties(addCourseDto, courseBase);//拷贝我们课程的基本信息
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseDto, courseMarket);//拷贝课程的营销信息
        String charge = addCourseDto.getCharge();
        if ("201001".equals(charge)) {
            if (addCourseDto.getPrice() == null) {
                //抛出我们的异常信息
                StudyException.cast("课程收费，价格必须收费");
            }
        }
        //
        boolean courseBaseFlag = this.save(courseBase);//保存到数据库
        courseMarket.setId(courseBase.getId());//添加完数据，会回显id的值
        //拿到我们的收费模式，如果为201001，表示是收费的，必须填入价格

        int insert = courseMarketMapper.insert(courseMarket);
        if (insert < 0 || !courseBaseFlag) {
            StudyException.cast(CommonError.UNKOWN_ERROR);
        }
        //封装一下查询的返回结果
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseId);
        return courseBaseInfo;
    }

    //sql语句:select * from courseBase where courseId=#{courseId}
    @Override
    public CourseBaseInfoDto getCourseIdByCourseInfoDTO(Long courseId) {
        if (courseId == null) {
            StudyException.cast("参数不能为null");
        }
        //根据课程id查询到课程信息
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseId);

        //然后根据id去查营销信息
        return courseBaseInfo;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseInfoDTO(Long companyId,UpdateCourseDTO updateCourseDto) {
        //校验一下数据库中数据存在
        CourseBase courseBase1 = courseBaseMapper.selectById(updateCourseDto.getId());
        if(courseBase1==null){
            StudyException.cast("课程不存在。。。。");
        }
        // TODO: 2023/2/21 后面在写上 
//        if(courseBase1.getCompanyId()!=companyId){
//            StudyException.cast("该教师不能删除本门课程.....");
//        }
        String charge = updateCourseDto.getCharge();
        if("201001".equals(charge)){//收费
            if (updateCourseDto.getPrice() == null) {
                //抛出我们的异常信息
                StudyException.cast("课程收费，价格必须收费");
            }
        }
        CourseBase courseBase = new CourseBase();
        courseBase.setChangeDate(LocalDateTime.now());//更新时间
        // TODO: 2023/2/20 更新人
        BeanUtils.copyProperties(updateCourseDto,courseBase);//封装courseBase
        CourseMarket courseMarket = new CourseMarket();
        //校验一下我们的营销表，有的话则进行修改、没有的话则进行添加
        CourseMarket selectMarket = courseMarketMapper.selectById(updateCourseDto.getId());
        BeanUtils.copyProperties(updateCourseDto,courseMarket);//封装courseMarket
        int f2=0;
        if(selectMarket!=null){
            //不等null就更新
            f2= courseMarketMapper.updateById(courseMarket);
        }else{
            //等于null就插入
            courseMarketMapper.insert(courseMarket);
        }
        int f1 = courseBaseMapper.updateById(courseBase);//保存课程信息

        if(f1<0||f2<0){
            StudyException.cast("更新失败");
        }
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(updateCourseDto.getId());
        return courseBaseInfo;
    }

    /**
     * 封装返回课程信息
     *
     * @param courseId
     * @return
     */
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId) {
        //先查我们的课程基本信息
        CourseBase courseBaseDao = courseBaseMapper.selectById(courseId);
        if (courseBaseDao == null) {
            StudyException.cast("参数类型错误");
        }
        CourseMarket courseMarketDao = this.courseMarketMapper.selectById(courseId);
        if (courseMarketDao == null) {
            StudyException.cast("参数类型错误");
        }
        //在查我们的课程营销信息
        //封装返回课程信息
        CourseBaseInfoDto infoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBaseDao, infoDto);
        BeanUtils.copyProperties(courseMarketDao, infoDto);
        //封装一下分类名称
        String mt = courseBaseDao.getMt();//大分类
        String st = courseBaseDao.getSt();//小分类
        if (StringUtils.isNotEmpty(mt)) {
            CourseCategory mtName = categoryMapper.selectById(mt);
            infoDto.setMtName(mtName.getName());
        }
        if (StringUtils.isNotEmpty(st)) {
            CourseCategory category = categoryMapper.selectById(st);
            infoDto.setStName(category.getName());
        }
        return infoDto;
    }
}
