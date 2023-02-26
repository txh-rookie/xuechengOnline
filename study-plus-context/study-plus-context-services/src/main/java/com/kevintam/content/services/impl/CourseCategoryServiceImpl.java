package com.kevintam.content.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.content.mapper.CourseCategoryMapper;
import com.kevintam.content.services.CourseBaseService;
import com.kevintam.content.services.CourseCategoryService;
import com.kevintam.dto.CourseCategoryDTO;
import com.kevintam.dto.SearchCourseNameDTO;
import com.kevintam.entity.CourseCategory;
import com.kevintam.study.entity.PageParams;
import com.kevintam.study.entity.PageResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/19
 */
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {

    /**
     * 以树的形式展示课程分类
     * @return
     */
    @Override
    public List<CourseCategory> categoryTreeNodes() {
        //先拿到所有的根节点
        List<CourseCategory> parentNode = this.list(new QueryWrapper<CourseCategory>().eq("parentid", 0).eq("is_show",1));
        //根据父节点，去查询所有的二级节点
        List<CourseCategory> resultCategoryTree=new ArrayList<>();
        //去判断一下我们的节点是否过期
        if(parentNode.size()>0&&!parentNode.isEmpty()){
            parentNode.forEach(category->{
                //去拿到所有的二级节点
                List<CourseCategory> twoNode = this.list(new QueryWrapper<CourseCategory>().eq("parentid", category.getId()).eq("is_show",1).orderByAsc("orderby"));
//            category.setChildrenTreeNodes(twoNode);
                List<CourseCategoryDTO> twoNodeDTO=new ArrayList<>();
                resultCategoryTree.addAll(twoNode);
                //去拿第三级节点
                resultCategoryTree.forEach(twoCategory->{
                    List<CourseCategory> tertiaryNode = this.list(new QueryWrapper<CourseCategory>().eq("parentid", twoCategory.getId()).eq("is_show",1).orderByAsc("orderby"));
                    twoCategory.setChildrenTreeNodes(tertiaryNode);
                });
            });
        }
        //拿到所有的数据之后封装到result中
        return resultCategoryTree;
    }

    @Override
    public CourseCategory getCourseCategoryNodes(Long courseId) {

        return null;
    }
}
