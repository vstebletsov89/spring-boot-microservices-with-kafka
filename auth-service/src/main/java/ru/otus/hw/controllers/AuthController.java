package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.LoginRequestDto;
import ru.otus.hw.dto.LoginResponseDto;
import ru.otus.hw.dto.SignupRequestDto;
import ru.otus.hw.dto.SignupResponseDto;
import ru.otus.hw.services.UserService;


@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;

    @PostMapping("/api/v1/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignupResponseDto signup (@RequestBody @Valid SignupRequestDto signupRequestDto) {
        log.info("signup: {}", signupRequestDto);
        return userService.signup(signupRequestDto);
    }

    @PostMapping("/api/v1/auth/login")
    public LoginResponseDto login (@RequestBody @Valid LoginRequestDto loginRequestDto) {
        log.info("login: {}", loginRequestDto);
        return userService.login(loginRequestDto);
    }
}