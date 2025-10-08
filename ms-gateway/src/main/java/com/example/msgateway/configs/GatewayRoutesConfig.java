package com.example.msgateway.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth-service/**")
                        .filters(f -> f.rewritePath(
                                "/api/auth-service/(?<segment>.*)",
                                "/api/${segment}"
                        ))
                        .uri("http://localhost:8081"))
                .build();
    }
}
