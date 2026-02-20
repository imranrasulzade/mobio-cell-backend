package com.example.msgateway.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {
    @Value("${application.security.internal-key}")
    private String internalKey;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth-service/**")
                        .filters(f -> f.rewritePath(
                                "/api/auth-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8081"))

                .route("user-service", r -> r
                        .path("/api/user-service/**")
                        .filters(f -> f.rewritePath(
                                "/api/user-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8082"))

                .route("number-service", r -> r
                        .path("/api/number-service/**")
                        .filters(f -> f.rewritePath(
                                "/api/number-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8083"))

                .route("balance-service", r -> r
                        .path("/api/balance-service/**")
                        .filters(f -> f.rewritePath(
                                "/api/balance-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8084"))

                .route("package-service", r -> r
                        .path("/api/package-service/**")
                        .filters(f -> f.rewritePath(
                                "/api/package-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8085"))

                .route("billing-service", r -> r
                        .path("/api/billing-service/**")
                        .filters(f -> f.rewritePath(
                                "/api/billing-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8086"))

                .route("notification-service", r -> r
                        .path("/api/notification-service/**")
                        .filters(f -> f.rewritePath(
                                "/api/notification-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8087"))

                .build();
    }
}
