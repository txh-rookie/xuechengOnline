package com.kevintam.content.api;

import com.kevintam.content.services.TeachplanService;
import com.kevintam.dto.AddTeachplanDTO;
import com.kevintam.dto.TeachplanDTO;
import com.kevintam.entity.Teachplan;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/20
 */
@Api("课程计划相关的接口")
@RestController
@Slf4j
public class TeachplanController {

    @Autowired
    private TeachplanService teachplanService;

    @ApiOperation("以树节点的形式查询课程计划")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDTO> getTeachplanDTONode(@PathVariable("courseId") Long courseId) {
        return teachplanService.selectTreeNodes(courseId);
    }

    @ApiOperation("新增课程计划的接口")
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody AddTeachplanDTO addTeachplanDTO) {
        //通过判断有没有课程计划id，来判断是新增还是修改接口
        teachplanService.saveTeachplan(addTeachplanDTO);
    }

    @DeleteMapping("/teachplan/{id}")
    public void deleteTeachplan(@PathVariable("id") String id) {
        teachplanService.deleteTeachplan(id);
    }
//    http://localhost:8601/api/content/teachplan/moveup/1628042532277137428
    @PostMapping("/teachplan/moveup/{id}")
    public void moveUpTeachplan(@PathVariable("id") String id){
        teachplanService.moveUp(id);
    }
}
