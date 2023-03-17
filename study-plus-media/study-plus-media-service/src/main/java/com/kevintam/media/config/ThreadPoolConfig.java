package com.kevintam.media.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/28
 * 定义一个线程池，来处理我们的处理编码
 */
@Configuration
public class ThreadPoolConfig {

    //设置一下核心数
    @Value("${ThreadPool.corePoolSize}")
    private Integer corePoolSize;
    //最大核心线程数
    @Value("${ThreadPool.maxCorePoolSize}")
    private Integer maxCorePoolSize;

    /**
     * 自定义线程池
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        return  new ThreadPoolExecutor(corePoolSize,
                maxCorePoolSize,10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                Executors.defaultThreadFactory(),
                //默认的拒接策略
                new ThreadPoolExecutor.AbortPolicy());
    }
}
