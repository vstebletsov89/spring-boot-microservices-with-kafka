package ru.otus.hw.kafka;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.OrderEventDto;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Getter
@Component
public class TestConsumer {

    private CountDownLatch latch = new CountDownLatch(1);

    private OrderEventDto consumedEvent;

    @KafkaListener(topics = "${application.kafka.topic}")
    public void receive(ConsumerRecord<String, OrderEventDto> consumerRecord) {
        consumedEvent = consumerRecord.value();
        latch.countDown();
    }

}
