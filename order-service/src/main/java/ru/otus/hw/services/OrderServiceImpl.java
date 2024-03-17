package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.ItemDto;
import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.dto.OrderEventDto;
import ru.otus.hw.dto.OrderState;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.kafka.ProducerService;
import ru.otus.hw.mappers.OrderMapper;
import ru.otus.hw.models.Item;
import ru.otus.hw.models.Order;
import ru.otus.hw.repositories.ItemRepository;
import ru.otus.hw.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final ProducerService producerService;

    private final OrderRepository orderRepository;

    private final ItemRepository itemRepository;

    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    @Override
    public OrderDto findOrderByOrderNumber(String orderNumber) {
        log.info("find order {}", orderNumber);
        return orderMapper.toDto(
                orderRepository.findOrderByOrderNumber(orderNumber)
                        .orElseThrow(() ->
                                new NotFoundException("Order with number '%s' not found".formatted(orderNumber))));
    }

    @Transactional
    @Override
    public OrderDto create(OrderCreateDto orderCreateDto) {
        log.info("create order {}", orderCreateDto);
        var newOrder =  new Order();

        var orderItems = itemRepository.findByIdIn(
                orderCreateDto.getItems()
                        .stream()
                        .map(ItemDto::getId)
                        .toList());
        log.info("order items {}", orderItems);

        newOrder.setUserId(orderCreateDto.getUserId());
        newOrder.setItems(orderItems);
        newOrder.setOrderNumber(UUID.randomUUID().toString());
        newOrder.setState(OrderState.CREATED);
        newOrder.setCreatedAt(LocalDateTime.now());
        // reserve item in stock
        newOrder.getItems()
                .forEach(i -> i.setQuantity(i.getQuantity() - 1));

        var amount = newOrder.getItems()
                .stream()
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderMapper.toDto(orderRepository.save(newOrder));

        var orderEvent = new OrderEventDto(
                newOrder.getUserId(),
                newOrder.getState().toString(),
                newOrder.getOrderNumber(),
                amount);

        producerService.sendOrderEvent(orderEvent);
        newOrder.setState(OrderState.PUBLISHED);

        return orderMapper.toDto(orderRepository.save(newOrder));
    }

    @Override
    public List<Order> getUnpaidOrders() {
        return orderRepository.findOrderByState(OrderState.PUBLISHED);
    }

    @Transactional
    @Override
    public void cancelByOrderNumber(String orderNumber) {
        log.info("cancel order {}", orderNumber);
        var cancelledOrder =  orderRepository.findOrderByOrderNumber(orderNumber)
                .orElseThrow(() ->
                        new NotFoundException("Order with number '%s' not found".formatted(orderNumber)));
        cancelledOrder.setState(OrderState.CANCELED);

        // remove reserve item in stock
        cancelledOrder.getItems()
                .forEach(i -> i.setQuantity(i.getQuantity() + 1));
        orderRepository.save(cancelledOrder);

        var orderEvent = new OrderEventDto(
                cancelledOrder.getUserId(),
                cancelledOrder.getState().toString(),
                cancelledOrder.getOrderNumber(),
                BigDecimal.ZERO);

        producerService.sendOrderEvent(orderEvent);
    }

}
