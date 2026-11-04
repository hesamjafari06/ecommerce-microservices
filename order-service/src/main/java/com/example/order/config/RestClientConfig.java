package com.example.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient OrderRestClient(){
        return RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

}
