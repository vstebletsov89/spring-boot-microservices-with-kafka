package ru.otus.hw.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder
public class ResponseServerMessage {
    private final String errorMessage;

    private final String stackTrace;
}