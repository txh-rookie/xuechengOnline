package com.kevintam.content;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.kevintam.content.services.CourseBaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/18
 */
@SpringBootTest(classes = ContentServiceTest.class)
public class ContentServiceTest {

//    @Autowired
//    private CourseBaseService service;

    @Resource
    private ElasticsearchClient client;

    @Test
    public void setService(){
//        service.listCourse()
        System.out.println(client);
    }
}
