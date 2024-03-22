package ru.otus.hw.services;


import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;

public interface OrderServiceClient {

    OrderDto findOrderByOrderNumber(String orderNumber);

    OrderDto create(OrderCreateDto orderCreateDto);

    String cancelByOrderNumber(String orderNumber);

}
