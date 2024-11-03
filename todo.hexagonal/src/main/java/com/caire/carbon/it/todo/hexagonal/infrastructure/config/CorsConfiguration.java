package com.caire.carbon.it.todo.hexagonal.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/todos/**")
                .allowedOrigins(
                        "http://localhost:4200",
                        "http://localhost:8080",
                        "http://localhost:4000"
                ).allowedHeaders(
                HttpHeaders.ACCEPT,
                HttpHeaders.CONTENT_TYPE
        );
    }

}
