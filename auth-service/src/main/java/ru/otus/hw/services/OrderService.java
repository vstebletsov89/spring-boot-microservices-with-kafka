package ru.otus.hw.services;

import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.models.Order;

import java.util.List;

public interface OrderService {
    OrderDto findOrderByOrderNumber(String orderNumber);

    OrderDto create(OrderCreateDto orderCreateDto);

    List<Order> getUnpaidOrders();

    void cancelByOrderNumber(String orderNumber);
}
