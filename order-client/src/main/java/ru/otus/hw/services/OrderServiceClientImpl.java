package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.feign.OrderServiceProxy;

@RequiredArgsConstructor
@Service
public class OrderServiceClientImpl implements OrderServiceClient {
    private final OrderServiceProxy orderServiceProxy;


    @Override
    public OrderDto findOrderByOrderNumber(String orderNumber) {
        return orderServiceProxy.getOrderByOrderNumber(orderNumber);
    }

    @Override
    public OrderDto create(OrderCreateDto orderCreateDto) {
        return orderServiceProxy.createOrder(orderCreateDto);
    }

    @Override
    public String cancelByOrderNumber(String orderNumber) {
        return orderServiceProxy.cancelOrder(orderNumber);
    }
}
