package com.kevintam.content.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/3/2
 */
@Configuration
public class RabbitMqConfig {

    /**
     * 定义用于处理的课程发布的交换机
     */
    public static final String COURSE_EXCHANGE="course-exchange";

    public static final String COURSE_QUEUE_REDIS="course_queue_add_redis";

    public static final String COURSE_QUEUE_ES="course_queue_add_es";

    /**
     * 声明一个直接类型交换机
     * @return
     */
    @Bean("course-exchange")
    public Exchange exchange(){
        return new FanoutExchange(COURSE_EXCHANGE, true, false);
    }

    /**
     * 生命一个队列用于处理往我们的redis中写入缓存
     * @return
     */
    @Bean("redis-queue")
    public Queue queue1(){
        return QueueBuilder.durable(COURSE_QUEUE_REDIS).build();
    }

    /**
     * 生命一个队列用于处理往我们的redis中写入缓存
     * @return
     */
    @Bean("es-queue")
    public Queue queue2(){
        return QueueBuilder.durable(COURSE_QUEUE_ES).build();
    }

    @Bean
    public Binding binding(@Qualifier("redis-queue")
                                       Queue queue1,
                           @Qualifier("course-exchange")Exchange change){
        return BindingBuilder.bind(queue1).to(change).with("").noargs();
    }
    /**
     * 声明了两个交换机来进行解决
     */
    @Bean
    public Binding binding2(@Qualifier("es-queue")
                                   Queue queue,
                           @Qualifier("course-exchange")Exchange change){
        return BindingBuilder.bind(queue).to(change).with("").noargs();
    }

}
