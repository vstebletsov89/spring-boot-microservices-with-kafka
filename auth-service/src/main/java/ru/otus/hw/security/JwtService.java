package ru.otus.hw.security;

import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.hw.models.User;

public interface JwtService {

    String generateToken(User user);

    String getUsernameFromToken(String token);

    boolean validateToken(String token, UserDetails userDetails);

}
