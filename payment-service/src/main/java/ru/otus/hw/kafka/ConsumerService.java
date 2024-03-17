package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.OrderEventDto;
import ru.otus.hw.dto.OrderState;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {
    //TODO: move Cinsumer and producer to service class

    private final ProducerService producerService;

    @KafkaListener(topics = "${application.kafka.input-topic}", containerFactory="containerFactoryPaymentService")
    public void consume(@Payload OrderEventDto eventDto) throws Exception {
        log.info("Got event from orders topic: {}", eventDto);
        //TODO: add saving to db

        eventDto.setState(OrderState.COMPLETED.toString());
        producerService.sendProcessedOrderEvent(eventDto);
        log.info("Order {} was processed for user {} amount: {}",
                eventDto.getOrderNumber(),
                eventDto.getUserId(),
                eventDto.getAmount());
    }

}