package ru.otus.hw.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = JwtServiceImpl.class)
class JwtServiceImplTest {

    @Value("${application.jwt.signing-key}")
    private String jwtSigningKey;

    @Autowired
    private JwtService jwtService;

    String generateToken(long id, String username, Role role) {
        var user = new User(id,
                username,
                "123456",
                role,
                LocalDateTime.now(),
                true);


        return jwtService.generateToken(user);
    }

    @DisplayName("должен возвращать токен с пользовательскими данными")
    @Test
    void shouldGenerateToken() {
        var token = generateToken(1, "test@mail.com", Role.USER);
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        var key = Keys.hmacShaKeyFor(keyBytes);
        var claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals("test@mail.com", claims.getSubject());
        assertEquals(1, claims.getOrDefault("user_id", "undefined"));
        assertEquals("USER", claims.getOrDefault("role", "undefined"));
    }

    @DisplayName("должен возвращать имя пользователя")
    @Test
    void shouldGetUsernameFromToken() {
        var token = generateToken(2L, "test2@mail.com", Role.USER);

        assertEquals("test2@mail.com", jwtService.getUsernameFromToken(token));
    }

    @DisplayName("валидация токена должна быть успешной")
    @Test
    void checkValidToken() {
        var userName = "test3@mail.com";
        var userRole = Role.USER;
        var token = generateToken(3L, userName, userRole);
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(userName)
                .password("123456")
                .authorities(new SimpleGrantedAuthority(userRole.toString()))
                .build();

        assertTrue(jwtService.validateToken(token, userDetails));
    }

    @DisplayName("валидация токена должна быть неуспешной")
    @Test
    void checkInvalidToken() {
        var userName = "test4@mail.com";
        var userRole = Role.USER;
        var token = generateToken(3L, userName, userRole);
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("anotherUser")
                .password("123456")
                .authorities(new SimpleGrantedAuthority(userRole.toString()))
                .build();

        assertFalse(jwtService.validateToken(token, userDetails));
    }
}