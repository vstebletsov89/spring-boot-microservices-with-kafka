package ru.otus.hw.security;

public class JwtService {

    String generateToken(UserDetails userDetails);
    String extractUserName(String token);

    boolean isTokenValid(String token, UserDetails userDetails);
}
