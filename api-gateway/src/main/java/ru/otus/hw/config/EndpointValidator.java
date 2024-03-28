package ru.otus.hw.config;

import lombok.Getter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
@Getter
public class EndpointValidator {

    private final Predicate<ServerHttpRequest> privateEndpoint =
            request -> Stream.of(
                            "auth/signup",
                            "auth/login"
                    )
                    .noneMatch(uri -> request
                            .getURI()
                            .getPath()
                            .contains(uri));
}
