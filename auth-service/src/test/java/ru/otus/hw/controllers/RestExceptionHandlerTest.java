package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.LoginRequestDto;
import ru.otus.hw.dto.SignupRequestDto;
import ru.otus.hw.exceptions.DuplicateUserException;
import ru.otus.hw.models.ResponseServerMessage;
import ru.otus.hw.repositories.UserRepository;
import ru.otus.hw.security.JwtService;
import ru.otus.hw.services.UserService;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка работы глобального обработчика ошибок")
@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureMockMvc
class RestExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthController authController;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @Autowired
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserRepository userRepository;

    @DisplayName("должен обрабатывать исключение, когда пользователь не найден")
    @Test
    void shouldHandleNotFoundExceptionForUser() throws Exception {
        doReturn(Optional.empty())
                .when(userRepository)
                .findByUsername(anyString());
        var request = new LoginRequestDto(
                "testUser@mail.com",
                "123456");
        var expectedResponse = ResponseServerMessage.builder()
                .errorMessage("User 'testUser@mail.com' not found")
                .stackTrace(null);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(userRepository, times(1))
                .findByUsername(anyString());
    }

    @DisplayName("должен обрабатывать исключениe внутренней ошибки сервера")
    @Test
    void shouldHandleDuplicateException() throws Exception {
        when(userRepository.findByUsername(anyString()))
                .thenThrow(new DuplicateUserException("User with the name testUser@mail.com already exists"));
        var request = new SignupRequestDto(
                "testUser@mail.com",
                "123456");

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content()
                        .string(containsString("errorMessage\":\"User with the name testUser@mail.com already exists")));

    }

    @DisplayName("должен обрабатывать исключениe внутренней ошибки сервера")
    @Test
    void shouldHandleAnyRuntimeException() throws Exception {
        when(userRepository.findByUsername(anyString()))
                .thenThrow(new RuntimeException());
        var request = new SignupRequestDto(
                "testUser@mail.com",
                "123456");

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content()
                        .string(containsString("stackTrace\":\"java.lang.RuntimeException")));

    }
}