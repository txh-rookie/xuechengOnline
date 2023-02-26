package com.kevintam.content.services;

import com.kevintam.dto.AddTeachplanDTO;
import com.kevintam.dto.TeachplanDTO;

import java.util.List;

public interface TeachplanService {
    //查询课程计划以树的形式，进行查询
   public List<TeachplanDTO> selectTreeNodes(Long courseId);

    void saveTeachplan(AddTeachplanDTO addTeachplanDTO);

    void deleteTeachplan(String id);

    void moveUp(String id);
}
