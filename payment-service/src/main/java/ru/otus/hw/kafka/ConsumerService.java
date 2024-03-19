package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.OrderEventDto;
import ru.otus.hw.dto.OrderState;
import ru.otus.hw.models.TransactionType;
import ru.otus.hw.services.PaymentService;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {

    private final ProducerService producerService;

    private final PaymentService paymentService;

    @KafkaListener(topics = "${application.kafka.input-topic}", containerFactory="containerFactoryPaymentService")
    public void consume(@Payload OrderEventDto eventDto, Acknowledgment acknowledgment) {
        log.info("Got event from orders topic: {}", eventDto);
        //TODO: add scheduler to process CREATED and REFUND payments each 5 minutes
        if (eventDto.getState().equals(OrderState.CREATED.toString())) {
            paymentService.addNewPaymentOperation(eventDto);
        } else if (eventDto.getState().equals(OrderState.CANCELED.toString())) {
            paymentService.updatePaymentStatus(eventDto.getOrderNumber(),
                    TransactionType.REFUND);
        } else {
            // send failed order to Dead Letter Queue
            log.error("Invalid order state: {}", eventDto.getState());
            producerService.sendFailedOrderEvent(eventDto);
            acknowledgment.acknowledge();
        }

        eventDto.setState(OrderState.COMPLETED.toString());
        producerService.sendProcessedOrderEvent(eventDto);
        log.info("Order {} was processed for user {} amount: {}",
                eventDto.getOrderNumber(),
                eventDto.getUserId(),
                eventDto.getAmount());

        //manual commit
        acknowledgment.acknowledge();
    }
}