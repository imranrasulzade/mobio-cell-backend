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

                .route("user-service", r -> r
                        .path("/api/user-service/**")
                        .filters(f -> f.rewritePath(
                                "/api/user-service/(?<segment>.*)",
                                "/api/${segment}"
                        ))
                        .uri("http://localhost:8082"))

                .route("number-service", r -> r
                        .path("/api/number-service/**")
                        .filters(f -> f.rewritePath(
                                "/api/number-service/(?<segment>.*)",
                                "/api/${segment}"
                        ))
                        .uri("http://localhost:8083"))

                .build();
    }
}
