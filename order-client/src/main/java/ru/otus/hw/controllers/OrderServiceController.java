package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.services.OrderServiceClient;


@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderServiceController {
    private final OrderServiceClient orderServiceClient;


    @GetMapping("/api/v1/orders/{orderNumber}")
    public OrderDto getOrderByOrderNumber(@PathVariable("orderNumber") String number) {
        log.info("findOrderByOrderNumber {}", number);
        return orderServiceClient.findOrderByOrderNumber(number);
    }

    @PostMapping("/api/v1/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@RequestBody @Valid OrderCreateDto orderCreateDto) {
        log.info(orderCreateDto.toString());
        return orderServiceClient.create(orderCreateDto);
    }

    @PutMapping("/api/v1/orders/{orderNumber}")
    public String cancelOrder (@PathVariable("orderNumber") String orderNumber) {
        log.info("cancelByOrderNumber {}", orderNumber);
        orderServiceClient.cancelByOrderNumber(orderNumber);
        return "Order was cancelled";
    }
}