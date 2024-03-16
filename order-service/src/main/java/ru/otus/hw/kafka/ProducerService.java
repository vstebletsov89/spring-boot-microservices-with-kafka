package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.OrderEvent;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerService {

    @Value("${application.kafka.topic}")
    private String topic;

    private final KafkaTemplate<String , OrderEvent> kafkaTemplate;

    public void sendOrderEvent(final OrderEvent orderEvent) {

        log.info("Send event to orders topic: {}", orderEvent);
        final ProducerRecord<String, OrderEvent> record =
                new ProducerRecord<>(topic,
                        String.valueOf(orderEvent.getUserId()),
                        orderEvent);

        var future = kafkaTemplate.send(record);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Order: {} was sent to Kafka topic", orderEvent);
            }
            else {
                log.error("Error while sending order event", ex);
            }
        });
    }
}