package ru.otus.hw.security;

import ru.otus.hw.dto.LoginRequestDto;
import ru.otus.hw.dto.LoginResponseDto;
import ru.otus.hw.dto.SignupRequestDto;
import ru.otus.hw.dto.SignupResponseDto;

public interface UserService {

    SignupResponseDto signup(SignupRequestDto signupRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

}