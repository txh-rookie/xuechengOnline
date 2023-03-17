package com.kevintam.content.api;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.kevintam.content.services.CourseAuditService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/3/2
 */
@RestController
@Api("课程发布接口")
public class CourseAuditController {

    @Autowired
    private CourseAuditService courseAuditService;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @ResponseBody
    @PostMapping("/courseaudit/commit/{courseId}")
    public void commitAudit(@PathVariable("courseId") Long courseId) {
        Long companyId=22L;
        courseAuditService.commitAudit(courseId, companyId);
    }
    @GetMapping("/elasticsearch")
    public String elasticsearch(){
        System.out.println(elasticsearchClient);
        return "访问elasticsearch";
    }
}