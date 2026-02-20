package com.example.msauth.configs;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignInternalAuthConfig {

    @Bean
    public RequestInterceptor internalKeyInterceptor(@Value("${application.security.internal-key}") String internalKey) {
        return template -> template.header("X-Internal-Key", internalKey);
    }
}
