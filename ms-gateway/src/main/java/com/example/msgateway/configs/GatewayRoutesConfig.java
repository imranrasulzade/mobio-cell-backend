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
                .route("auth-service-docs", r -> r
                        .path("/v3/api-docs/auth-service")
                        .filters(f -> f.preserveHostHeader().rewritePath("/v3/api-docs/auth-service", "/v3/api-docs")
                                .addRequestHeader("X-Internal-Key", internalKey)
                                .addRequestHeader("X-Forwarded-Prefix", "/api/auth-service"))
                        .uri("http://localhost:8081"))

                .route("user-service-docs", r -> r
                        .path("/v3/api-docs/user-service")
                        .filters(f -> f.preserveHostHeader().rewritePath("/v3/api-docs/user-service", "/v3/api-docs")
                                .addRequestHeader("X-Internal-Key", internalKey)
                                .addRequestHeader("X-Forwarded-Prefix", "/api/user-service"))
                        .uri("http://localhost:8082"))

                .route("number-service-docs", r -> r
                        .path("/v3/api-docs/number-service")
                        .filters(f -> f.preserveHostHeader().rewritePath("/v3/api-docs/number-service", "/v3/api-docs")
                                .addRequestHeader("X-Internal-Key", internalKey)
                                .addRequestHeader("X-Forwarded-Prefix", "/api/number-service"))
                        .uri("http://localhost:8083"))

                .route("balance-service-docs", r -> r
                        .path("/v3/api-docs/balance-service")
                        .filters(f -> f.preserveHostHeader().rewritePath("/v3/api-docs/balance-service", "/v3/api-docs")
                                .addRequestHeader("X-Internal-Key", internalKey)
                                .addRequestHeader("X-Forwarded-Prefix", "/api/balance-service"))
                        .uri("http://localhost:8084"))

                .route("package-service-docs", r -> r
                        .path("/v3/api-docs/package-service")
                        .filters(f -> f.preserveHostHeader().rewritePath("/v3/api-docs/package-service", "/v3/api-docs")
                                .addRequestHeader("X-Internal-Key", internalKey)
                                .addRequestHeader("X-Forwarded-Prefix", "/api/package-service"))
                        .uri("http://localhost:8085"))

                .route("billing-service-docs", r -> r
                        .path("/v3/api-docs/billing-service")
                        .filters(f -> f.preserveHostHeader().rewritePath("/v3/api-docs/billing-service", "/v3/api-docs")
                                .addRequestHeader("X-Internal-Key", internalKey)
                                .addRequestHeader("X-Forwarded-Prefix", "/api/billing-service"))
                        .uri("http://localhost:8086"))

                .route("notification-service-docs", r -> r
                        .path("/v3/api-docs/notification-service")
                        .filters(f -> f.preserveHostHeader().rewritePath("/v3/api-docs/notification-service", "/v3/api-docs")
                                .addRequestHeader("X-Internal-Key", internalKey)
                                .addRequestHeader("X-Forwarded-Prefix", "/api/notification-service"))
                        .uri("http://localhost:8087"))

                .route("auth-service", r -> r
                        .path("/api/auth-service/api/auth/**")
                        .filters(f -> f.preserveHostHeader().rewritePath(
                                "/api/auth-service/api/auth/(?<segment>.*)",
                                "/api/auth/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8081"))

                .route("auth-service-legacy", r -> r
                        .path("/api/auth-service/auth/**")
                        .filters(f -> f.preserveHostHeader().rewritePath(
                                "/api/auth-service/auth/(?<segment>.*)",
                                "/api/auth/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8081"))

                .route("auth-service-direct", r -> r
                        .path("/api/auth/**")
                        .filters(f -> f.preserveHostHeader()
                                .addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8081"))

                .route("user-service", r -> r
                        .path("/api/user-service/**")
                        .filters(f -> f.preserveHostHeader().rewritePath(
                                "/api/user-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8082"))

                .route("number-service", r -> r
                        .path("/api/number-service/**")
                        .filters(f -> f.preserveHostHeader().rewritePath(
                                "/api/number-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8083"))

                .route("balance-service", r -> r
                        .path("/api/balance-service/**")
                        .filters(f -> f.preserveHostHeader().rewritePath(
                                "/api/balance-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8084"))

                .route("package-service", r -> r
                        .path("/api/package-service/**")
                        .filters(f -> f.preserveHostHeader().rewritePath(
                                "/api/package-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8085"))

                .route("billing-service", r -> r
                        .path("/api/billing-service/**")
                        .filters(f -> f.preserveHostHeader().rewritePath(
                                "/api/billing-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8086"))

                .route("notification-service", r -> r
                        .path("/api/notification-service/**")
                        .filters(f -> f.preserveHostHeader().rewritePath(
                                "/api/notification-service/(?<segment>.*)",
                                "/api/${segment}"
                        ).addRequestHeader("X-Internal-Key", internalKey))
                        .uri("http://localhost:8087"))

                .build();
    }
}
