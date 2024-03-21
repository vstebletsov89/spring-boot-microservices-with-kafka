package ru.otus.hw.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.LoginRequestDto;
import ru.otus.hw.dto.LoginResponseDto;
import ru.otus.hw.dto.SignupRequestDto;
import ru.otus.hw.dto.SignupResponseDto;
import ru.otus.hw.exceptions.DuplicateUserException;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;

import java.security.Key;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SignatureAlgorithm.HS256);

    private static final int MINUTES = 60;

    private final UserRepository userRepository;

    // custom BcryptPasswordEncoder is used
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

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
        user.setRole(Role.USER.toString());
        user.setActive(true);
        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);
        return new SignupResponseDto(user.getUsername(), "User was successfully created");
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()));
        //TOOD: implement it

    }
}