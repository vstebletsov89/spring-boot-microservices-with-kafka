package ru.otus.hw.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Order;
import ru.otus.hw.services.OrderService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderCancelSchedule {

    private final long OUTDATED_INTERVAL_MINUTES = 20;

    private final OrderService orderService;

    @Scheduled(fixedRate = 60000)
    public void cancelUnpaidOrders() {
        log.info("Scheduler to cancel unpaid orders");
        List<Order> unpaidOrders = orderService.getUnpaidOrders();
        unpaidOrders.forEach(order -> {
            if (order.getCreatedAt().plusMinutes(OUTDATED_INTERVAL_MINUTES).isBefore(LocalDateTime.now())) {
                orderService.cancelByOrderNumber(order.getOrderNumber());
            }
        });
    }
}