package com.scm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class AppConfig {
    @Value("${cloudinary.cloud.name}")
    private String cloudName;

    @Value("${cloudinary.cloud.key}")
    private String cloudKey;

    @Value("${cloudinary.cloud.secret}")
    private String cloudSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", cloudKey,
                        "api_secret", cloudSecret));
    }

}
