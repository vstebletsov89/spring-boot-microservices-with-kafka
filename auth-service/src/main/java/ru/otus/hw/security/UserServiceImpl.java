package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.SignupRequestDto;
import ru.otus.hw.exceptions.DuplicateUserException;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // custom BcryptPasswordEncoder is used
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void signup(SignupRequestDto signupRequestDto) {
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
    }
}