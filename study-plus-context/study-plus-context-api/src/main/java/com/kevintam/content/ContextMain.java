package com.kevintam.content;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/18
 */
@SpringBootApplication
@EnableSwagger2Doc //swagger接口
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kevintam.study","com.kevintam.content"})
public class  ContextMain {
    public static void main(String[] args) {
        SpringApplication.run(ContextMain.class,args);
    }
}
