package ru.otus.hw.services;

import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;

public interface OrderService {
    OrderDto findOrderByCustomerNumber(String customerNumber);

    OrderDto create(OrderCreateDto orderCreateDto);

    void cancelByCustomerNumber(String customerNumber);
}
