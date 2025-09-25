package com.backend.weeklybite.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.serving-path}")
    private String servingPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Maps the URL path /images/** to the local file system (uploads folder)
        // file:///path/to/your/app/uploads/
        registry.addResourceHandler(servingPath + "**")
                .addResourceLocations("file:" + uploadDir + "/"); // Dodaj trailing slash
    }
}
