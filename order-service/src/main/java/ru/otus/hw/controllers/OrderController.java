package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.services.OrderService;


@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/api/v1/orders/{orderNumber}")
    public OrderDto getOrderByOrderNumber(@PathVariable("orderNumber") String number) {
        return orderService.findOrderByOrderNumber(number);
    }

    @PostMapping("/api/v1/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@RequestBody @Valid OrderCreateDto orderCreateDto) {
        log.info(orderCreateDto.toString());
        return orderService.create(orderCreateDto);
    }

    @PatchMapping("/api/v1/orders/{orderNumber}")
    public String cancelOrder (@PathVariable("orderNumber") String orderNumber) {
        orderService.cancelByOrderNumber(orderNumber);
        return "Order was cancelled";
    }
}