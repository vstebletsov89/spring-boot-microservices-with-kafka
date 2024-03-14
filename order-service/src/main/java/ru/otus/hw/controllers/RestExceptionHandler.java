package ru.otus.hw.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.ResponseServerMessage;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    ResponseServerMessage notFoundError(NotFoundException exception) {
        log.info("notFound exception: {}", exception.getMessage());

        return ResponseServerMessage.builder()
                .errorMessage(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    ResponseServerMessage internalError(Exception exception) {
        log.info("internalError exception: {}", exception.getMessage());

        return ResponseServerMessage.builder()
                .errorMessage(exception.getMessage())
                .stackTrace(ExceptionUtils.getStackTrace(exception))
                .build();
    }
}