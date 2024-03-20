package ru.otus.hw.security;

import ru.otus.hw.dto.SignupRequestDto;

public interface UserService {

    void signup(SignupRequestDto signupRequestDto);

}