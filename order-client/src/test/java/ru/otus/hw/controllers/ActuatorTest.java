package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка контроля доступа для actuator")
@SpringBootTest
@AutoConfigureMockMvc
public class ActuatorTest {
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("должен загружать actuator endpoints")
    @ParameterizedTest
    @ValueSource(strings = {
            "/actuator",
            "/actuator/circuitbreakers",
            "/actuator/circuitbreakerevents",
            "/actuator/ratelimiters",
            "/actuator/ratelimiterevents",
            "/actuator/retries",
            "/actuator/retryevents",
            "/actuator/timelimiters",
            "/actuator/timelimiterevents",
    })
    void shouldHaveAccessToActuator(String endpoint) throws Exception {
        mockMvc.perform(get(endpoint))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
