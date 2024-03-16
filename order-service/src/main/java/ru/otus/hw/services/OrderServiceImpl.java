package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.OrderMapper;
import ru.otus.hw.models.OrderState;
import ru.otus.hw.repositories.OrderRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    @Override
    public OrderDto findOrderByCustomerNumber(String customerNumber) {
        return orderMapper.toDto(
                orderRepository.findOrderByCustomerNumber(customerNumber)
                        .orElseThrow(() ->
                                new NotFoundException("Order with number '%s' not found".formatted(customerNumber))));
    }

    @Transactional
    @Override
    public OrderDto create(OrderCreateDto orderCreateDto) {
        var newOrder = orderMapper.toModel(orderCreateDto);
        newOrder.setCustomerNumber(UUID.randomUUID().toString());
        newOrder.setState(OrderState.CREATED);
        newOrder.setCreatedAt(LocalDateTime.now());
        var createdOrder = orderMapper.toDto(orderRepository.save(newOrder));
        //TODO: publish event to Kafka

        return createdOrder;
    }

    @Transactional
    @Override
    public void cancelByCustomerNumber(String customerNumber) {
        var order =  orderRepository.findOrderByCustomerNumber(customerNumber)
                .orElseThrow(() ->
                        new NotFoundException("Order with number '%s' not found".formatted(customerNumber)));
        order.setState(OrderState.CANCELED);
        orderRepository.save(order);
    }

}
