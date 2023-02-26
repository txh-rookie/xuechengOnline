package com.kevintam.content.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.content.mapper.TeachplanMapper;
import com.kevintam.content.mapper.TeachplanMediaMapper;
import com.kevintam.content.services.TeachplanService;
import com.kevintam.dto.AddTeachplanDTO;
import com.kevintam.dto.TeachplanDTO;
import com.kevintam.entity.Teachplan;
import com.kevintam.entity.TeachplanMedia;
import com.kevintam.study.exception.StudyException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/20
 */
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDTO> selectTreeNodes(Long courseId) {
        if (courseId == null) {
            StudyException.cast("参数不能为空");
        }
        List<TeachplanDTO> teachplanDTOS = teachplanMapper.selectTreeNodes(courseId);

        return teachplanDTOS;
    }

    /**
     * 实现我们的添加功能
     */
    @Override
    public void saveTeachplan(AddTeachplanDTO addTeachplanDTO) {
        if (addTeachplanDTO.getId() == null) {
            //就是一个新增计划
            addTeachplan(addTeachplanDTO);
        } else {
            //就是修改计划
            Long id = addTeachplanDTO.getId();
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(addTeachplanDTO,teachplan);
            System.out.println(teachplan);
            teachplanMapper.updateById(teachplan);
        }
    }

    @Transactional
    @Override
    public void deleteTeachplan(String id) {
        if(!StringUtils.isNotEmpty(id)){
            StudyException.cast("参数类型错误...");
        }
        Teachplan teachplan = teachplanMapper.selectById(id);
        if(teachplan==null){
            StudyException.cast("系统没有该课程计划，没有办法删除");
        }
        //删除的是子章节
        if(teachplan.getGrade()==2){
            int i = teachplanMapper.deleteById(id);
            QueryWrapper<TeachplanMedia> queryWrapper = new QueryWrapper<>();
            TeachplanMedia teachplanMedia= teachplanMediaMapper.selectById(queryWrapper.eq("teachplan_id", id));
            if(teachplanMedia!=null){
                //删除掉关联的视频
                teachplanMediaMapper.delete(queryWrapper.eq("teachplan_id",id));
            }
            if(i<0){
                StudyException.cast("删除信息失败");
            }
        }
        //如果是章节,则要将子章节一起删除
        if(teachplan.getGrade()==1){
            QueryWrapper<Teachplan> teachplanQueryWrapper = new QueryWrapper<>();
            QueryWrapper<TeachplanMedia> queryWrapper = new QueryWrapper<>();
            teachplanMapper.delete(teachplanQueryWrapper.eq("parentid",teachplan.getId()));
            TeachplanMedia teachplanMedia= teachplanMediaMapper.selectById(queryWrapper.eq("teachplan_id", id));
            if(teachplanMedia!=null){
                //删除掉关联的视频
                teachplanMediaMapper.delete(queryWrapper.eq("teachplan_id",id));
            }
        }
    }
    //实现小节上移动，或者是章节的下移操作
    @Override
    public void moveUp(String id) {
       if(StringUtils.isEmpty(id)){
           StudyException.cast("参数不能为空");
       }
        Teachplan teachplan = teachplanMapper.selectById(id);
       //判断一下移动的是二级节点还是一级节点
        moveOneLevel(teachplan);
    }
    //将二级移动一级
    private void moveOneLevel(Teachplan teachplan) {

    }
    //将一级移动到二级
    private void moveLevel2(Teachplan teachplan){
        if(teachplan.getGrade()!=1){
            StudyException.cast("参数不正确");
        }
        //将一级移动到二级
//        teachplan.setGrade(2);
//        //然后将我们的一级移动二级,拿到我们的父级id
//        QueryWrapper<Teachplan> teachplanQueryWrapper = new QueryWrapper<>();
//        Teachplan parentTeachplan = teachplanMapper.selectOne(teachplanQueryWrapper.eq("id", teachplan.getParentid()));

    }

    private void addTeachplan(AddTeachplanDTO addTeachplanDTO) {
        if (addTeachplanDTO.getGrade() == null) {
            StudyException.cast("参数类型错误");
        }
        //通过grade是以及课程计划还是二级课程计划
        Teachplan teachplan = new Teachplan();
        BeanUtils.copyProperties(addTeachplanDTO, teachplan);
        //课程的状态，肯定是未发布的
        teachplan.setStatus(1);
        //获取一个方法去获取orderby最新的
        Integer newOrderBy = getNewOrderBy(addTeachplanDTO.getParentid(), addTeachplanDTO.getCourseId());
        teachplan.setOrderby(++newOrderBy);
        //创建时间
        teachplan.setCreateDate(LocalDateTime.now());
        teachplanMapper.insert(teachplan);//添加课程计划
    }

    //    SELECT count(orderby) FROM teachplan where parentid=274 and course_id=117
    private Integer getNewOrderBy(Long parentid, Long courseId) {
        return teachplanMapper.getNewOrderBy(parentid, courseId);
    }

}
