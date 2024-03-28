package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final AuthGatewayFilter authGatewayFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.filter(authGatewayFilter))
                        .uri("lb://auth-service"))

                .route("order-client", r -> r.path("/api/v1/orders/**")
                        .filters(f -> f.filter(authGatewayFilter))
                        .uri("lb://order-client"))
                .build();
    }
}
