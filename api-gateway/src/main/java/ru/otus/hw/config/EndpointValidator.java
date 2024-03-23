package ru.otus.hw.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class EndpointValidator {

    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "auth/signup",
            "auth/login"
    );

    public Predicate<ServerHttpRequest> isPrivateEndpoint =
            request -> PUBLIC_ENDPOINTS
                    .stream()
                    .noneMatch(uri -> request
                            .getURI()
                            .getPath()
                            .contains(uri));
}
