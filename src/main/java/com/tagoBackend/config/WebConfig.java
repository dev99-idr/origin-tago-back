package com.tagoBackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// ** For CORS **
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry reg) {
        reg.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS")
                .maxAge(3600);
//                .allowCredentials(true);
//                .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "X-M2M-RI", "X-M2M-RVI", "X-M2M-RSC", "Accept", "X-M2M-Origin", "Locale")
//                .exposedHeaders("Origin", "X-Requested-With", "Content-Type", "X-M2M-RI", "X-M2M-RVI", "X-M2M-RSC", "Accept", "X-M2M-Origin", "Locale");
    }
}
