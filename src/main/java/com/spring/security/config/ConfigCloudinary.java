package com.spring.security.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.servlet.MultipartConfigElement;

@Configuration
public class ConfigCloudinary {
    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dcyemfp6o",
                "api_key", "454311765351397",
                "api_secret", "rFRoDyj6UsAXFli0o6y_Fh8DSRg",
                "secure", true));
        return cloudinary;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofBytes(Constants.maxFileSize));
        factory.setMaxRequestSize(DataSize.ofBytes(Constants.maxRequestFileSize));
        return factory.createMultipartConfig();
    }
}
