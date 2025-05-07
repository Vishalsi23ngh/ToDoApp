package com.example.toDoAppBackend.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Allow all API endpoints
                        .allowedOrigins("http://127.0.0.1:5500") // Allow frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS") // Allow specific HTTP methods
                        .allowedHeaders("*")
                        .allowCredentials(true); // Allow sending cookies (if needed)
            }
        };
    }
}