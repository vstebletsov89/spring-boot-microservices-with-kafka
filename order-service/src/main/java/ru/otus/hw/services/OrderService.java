package ru.otus.hw.services;

import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;

public interface OrderService {
    OrderDto findOrderByOrderNumber(String orderNumber);

    OrderDto create(OrderCreateDto orderCreateDto);

    void cancelByOrderNumber(String orderNumber);
}
