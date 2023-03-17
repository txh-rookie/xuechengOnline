package com.kevintam.media.config;

import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/23
 */
@Configuration
public class MinioClientConfig {
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;


    /**
     * 往ioc容器放入一个minioClient对象
     * @return
     */
    @Bean
    public MinioClient minioClient(){
        MinioClient build = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
        return build;
    }
}
