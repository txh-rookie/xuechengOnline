package com.kevintam.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kevintam.dto.TeachplanDTO;
import com.kevintam.entity.Teachplan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    public List<TeachplanDTO> selectTreeNodes(@Param("courseId") Long courseId);
    public Integer getNewOrderBy(@Param("parentid") Long parentid,@Param("courseId") Long courseId);
}
