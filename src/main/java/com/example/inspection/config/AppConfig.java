package com.example.inspection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Value("${app.export.dir}")
    private String exportDir;

    @Value("${app.export.url}")
    private String exportUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ánh xạ URL /exports/** tới thư mục uploads/exports
        registry.addResourceHandler(exportUrl)
                .addResourceLocations("file:" + exportDir + "/");
    }
}
