package com.takeitfree.itemmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for the application.
 * This class is used to define beans that can be used throughout the application.
 */
@Configuration
public class AppConfig {

    //used to make REST calls to external services
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
