package com.kevintam.content.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/3/8
 */
@Configuration
public class ElasticsearchClientConfig {

    @Bean
    public ElasticsearchClient restClient() {
        HttpHost httpHost = new HttpHost("localhost",9200);
        RestClient restClient = RestClient.builder(httpHost).build();
        //json的方式
        ElasticsearchTransport transport=new RestClientTransport(restClient,new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
