package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.LoginRequestDto;
import ru.otus.hw.dto.LoginResponseDto;
import ru.otus.hw.dto.SignupRequestDto;
import ru.otus.hw.dto.SignupResponseDto;
import ru.otus.hw.exceptions.DuplicateUserException;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;
import ru.otus.hw.security.JwtService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        String userName = signupRequestDto.getUsername();
        var existingUser = userRepository.findByUsername(userName);
        if (existingUser.isPresent()) {
            throw new DuplicateUserException("User with the name '%s' already exists".formatted(userName));
        }

        String hashPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        User user = new User();
        user.setUsername(signupRequestDto.getUsername());
        user.setPassword(hashPassword);
        user.setRole(Role.USER);
        user.setActive(true);
        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);
        return new SignupResponseDto(user.getUsername(), "User was successfully created");
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        var user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() ->
                        new NotFoundException("User '%s' not found".formatted(loginRequestDto.getUsername())));

        return new LoginResponseDto(user.getUsername(), jwtService.generateToken(user));
    }
}