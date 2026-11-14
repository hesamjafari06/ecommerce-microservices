package com.example.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient productServiceRestClient() {
        return RestClient.builder().baseUrl("http://localhost:8082").build();
    }

    @Bean
    public RestClient orderServiceRestClient() {
        return RestClient.builder().baseUrl("http://localhost:8083").build();
    }

}
