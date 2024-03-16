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
import ru.otus.hw.mappers.ItemMapperImpl;
import ru.otus.hw.mappers.OrderMapperImpl;
import ru.otus.hw.models.ResponseServerMessage;
import ru.otus.hw.repositories.OrderRepository;
import ru.otus.hw.services.OrderService;
import ru.otus.hw.services.OrderServiceImpl;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка работы глобального обработчика ошибок")
@SpringBootTest(classes = {
        OrderController.class,
        OrderServiceImpl.class,
        RestExceptionHandler.class,
        OrderMapperImpl.class,
        ItemMapperImpl.class
})
@AutoConfigureWebMvc
@AutoConfigureMockMvc
class RestExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderController orderController;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @DisplayName("должен обрабатывать исключение, когда заказ не найден")
    @Test
    void shouldHandleNotFoundExceptionForOrder() throws Exception {
        doReturn(Optional.empty())
                .when(orderRepository)
                .findOrderByCustomerNumber(anyString());
        var expectedResponse = ResponseServerMessage.builder()
                .errorMessage("Order with number '12345' not found")
                .stackTrace(null);

        mockMvc.perform(get("/api/v1/orders/{customerNumber}", "12345"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(orderRepository, times(1)).findOrderByCustomerNumber(anyString());
    }


    @DisplayName("должен обрабатывать исключениe внутренней ошибки сервера")
    @Test
    void shouldHandleAnyRuntimeException() throws Exception {
        when(orderRepository.findOrderByCustomerNumber(anyString())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v1/orders/{customerNumber}", "12345"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content()
                        .string(containsString("stackTrace\":\"java.lang.RuntimeException")));

    }
}