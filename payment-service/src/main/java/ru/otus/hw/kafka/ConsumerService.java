package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.OrderEventDto;
import ru.otus.hw.dto.OrderState;
import ru.otus.hw.services.PaymentService;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {

    private final ProducerService producerService;

    private final PaymentService paymentService;

    @KafkaListener(topics = "${application.kafka.input-topic}", containerFactory = "containerFactoryPaymentService")
    public void consume(@Payload OrderEventDto eventDto, Acknowledgment acknowledgment) {
        log.info("Got event from orders topic: {}", eventDto);
        try {
            OrderState orderState;
            if (eventDto.getState().equals(OrderState.CREATED.toString())) {
                paymentService.create(eventDto);
                paymentService.executePayment(eventDto.getOrderNumber(), eventDto.getUserId());
                orderState = OrderState.COMPLETED;
            } else if (eventDto.getState().equals(OrderState.CANCELED.toString())) {
                paymentService.cancel(eventDto.getOrderNumber());
                orderState = OrderState.CANCELED;
            } else {
                // send failed order to Dead Letter Queue
                log.error("Failed order state: {}", eventDto.getState());
                producerService.sendFailedOrderEvent(eventDto);
                acknowledgment.acknowledge();
                return;
            }
            processPayment(eventDto, orderState);
            acknowledgment.acknowledge(); // manual commit
        } catch (Exception ex) {
            log.error("Error while executing payment. Try again later", ex);
            acknowledgment.nack(Duration.ofSeconds(10)); // event will be redelivered
        }
    }

    private void processPayment(OrderEventDto eventDto, OrderState orderState) {
        // payment processed or cancelled, send event to processed topic
        eventDto.setState(orderState.toString());
        producerService.sendProcessedOrderEvent(eventDto);
        log.info("Order {} was processed for user {} amount: {}",
                eventDto.getOrderNumber(),
                eventDto.getUserId(),
                eventDto.getAmount());
    }
}