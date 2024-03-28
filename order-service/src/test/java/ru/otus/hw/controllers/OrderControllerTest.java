package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.ItemDto;
import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.dto.OrderState;
import ru.otus.hw.services.OrderService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка работы контроллера заказов")
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    private static List<ItemDto> expectedItems = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @BeforeAll
    static void setExpectedItems() {
        expectedItems = List.of(
                new ItemDto(1L),
                new ItemDto(2L),
                new ItemDto(3L));
    }

    @DisplayName("должен загружать заказ")
    @Test
    void shouldReturnOrderByOrderNumber() throws Exception {
        var expectedOrder = new OrderDto(
                "1a80ccd8-7777-4001-ba61-1942ec218377",
                OrderState.PUBLISHED.toString(),
                expectedItems);
        doReturn(expectedOrder)
                .when(orderService)
                .findOrderByOrderNumber(expectedOrder.getOrderNumber());

        mockMvc.perform(get("/api/v1/orders/{orderNumber}",
                        expectedOrder.getOrderNumber()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOrder)));

        verify(orderService, times(1))
                .findOrderByOrderNumber(expectedOrder.getOrderNumber());
    }

    @DisplayName("должен добавить заказ")
    @Test
    void shouldAddOrder() throws Exception {
        var orderCreateDto = new OrderCreateDto(1L, expectedItems);
        var expectedOrder = new OrderDto(
                "1a80ccd8-7777-4001-ba61-1942ec218377",
                OrderState.CREATED.toString(),
                expectedItems);
        doReturn(expectedOrder).when(orderService).create(orderCreateDto);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOrder)));

        verify(orderService, times(1)).create(orderCreateDto);
    }

    @DisplayName("должен отменить заказ")
    @Test
    void shouldCancelOrderByOrderNumber() throws Exception {
        doNothing()
                .when(orderService)
                .cancelByOrderNumber(anyString());

        mockMvc.perform(put("/api/v1/orders/{orderNumber}",
                        "12345"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string("Order was cancelled"));

        verify(orderService, times(1))
                .cancelByOrderNumber(anyString());
    }

}