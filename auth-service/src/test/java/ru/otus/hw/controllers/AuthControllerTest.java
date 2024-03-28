package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.LoginRequestDto;
import ru.otus.hw.dto.LoginResponseDto;
import ru.otus.hw.dto.SignupRequestDto;
import ru.otus.hw.dto.SignupResponseDto;
import ru.otus.hw.security.JwtService;
import ru.otus.hw.services.UserService;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка работы контроллера авторизации")
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private  JwtService jwtService;

    @DisplayName("должен регистрировать пользователя")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldReturnSignupResponse() throws Exception {
        var expectedResponse = new SignupResponseDto(
                "testUser@mail.com",
                "User was successfully created");
        var request = new SignupRequestDto(
                "testUser@mail.com",
                "123456");
        doReturn(expectedResponse)
                .when(userService)
                .signup(request);

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(userService, times(1))
                .signup(request);
    }

    @DisplayName("должен логинить пользователя")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldReturnLoginResponse() throws Exception {
        var expectedResponse = new LoginResponseDto(
                "testUser@mail.com",
                "jwt_token_here");
        var request = new LoginRequestDto(
                "testUser@mail.com",
                "123456");
        doReturn(expectedResponse)
                .when(userService)
                .login(request);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(userService, times(1))
                .login(request);
    }
}