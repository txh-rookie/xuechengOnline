package com.kevintam.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/19
 */
@SpringBootApplication
@EnableDiscoveryClient
public class StudySystemMain {
    public static void main(String[] args) {
        SpringApplication.run(StudySystemMain.class,args);
    }
}
