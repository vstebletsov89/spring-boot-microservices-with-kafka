package ru.otus.hw.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;

@FeignClient(name = "order-service")
public interface OrderServiceProxy {

    @CircuitBreaker(name = "getOrder", fallbackMethod = "buildFallbackOrder")
    @GetMapping("/api/v1/orders/{orderNumber}")
    OrderDto getOrderByOrderNumber(@PathVariable("orderNumber") String number);

    @SuppressWarnings("unused")
    default OrderDto buildFallbackOrder(Throwable throwable) {
        return new OrderDto("fallback_order_number", "unknown", null);
    }

    @CircuitBreaker(name = "createOrder", fallbackMethod = "buildFallbackOrder")
    @PostMapping("/api/v1/orders")
    OrderDto createOrder(@RequestBody @Valid OrderCreateDto orderCreateDto);

    @CircuitBreaker(name = "cancelOrder", fallbackMethod = "buildFallbackResponse")
    @PutMapping("/api/v1/orders/{orderNumber}")
    String cancelOrder (@PathVariable("orderNumber") String orderNumber);

    @SuppressWarnings("unused")
    default String buildFallbackResponse(Throwable throwable) {
        return "Server is unavailable. Try again later.";
    }

}