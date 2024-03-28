package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка actuator endpoints")
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1,
        brokerProperties =
                { "listeners=PLAINTEXT://localhost:9092",
                        "port=9092" })
@AutoConfigureMockMvc
public class ActuatorTest {
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("должен загружать actuator endpoints")
    @ParameterizedTest
    @ValueSource(strings = {
            "/actuator",
            "/actuator/logfile",
            "/actuator/health",
            "/actuator/flyway",
            "/actuator/metrics"})
    void shouldHaveAccessToActuator(String endpoint) throws Exception {
        mockMvc.perform(get(endpoint))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
