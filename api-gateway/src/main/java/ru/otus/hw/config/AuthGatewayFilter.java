package ru.otus.hw.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;

@RefreshScope
@RequiredArgsConstructor
@Component
public class AuthGatewayFilter implements GatewayFilter {

    @Value("${application.jwt.signing-key}")
    private String jwtSigningKey;

    private final EndpointValidator endpointValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // check user is authorized for private endpoints
        if (endpointValidator.isPrivateEndpoint.test(request)) {

            if (isAuthMissing(request)) {
                return authError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String token = getAuthHeader(request);
            Claims claims = getTokenPayload(token);
            boolean isInvalid =  claims.getExpiration().before(new Date());

            if (isInvalid) {
                return authError(exchange, HttpStatus.FORBIDDEN);
            }

            updateRequest(exchange, token);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> authError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void updateRequest(ServerWebExchange exchange, String token) {
        var claims = getTokenPayload(token);
        exchange.getRequest()
                .mutate()
                .header("userName", claims.getSubject())
                .header("userId",
                        String.valueOf(claims.getOrDefault("user_id", "undefined")))
                .header("role",
                        String.valueOf(claims.getOrDefault("role", "undefined")))
                .build()
        ;
    }

    private Claims getTokenPayload(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
