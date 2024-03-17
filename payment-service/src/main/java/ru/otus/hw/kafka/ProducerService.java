package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.OrderEventDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerService {

    @Value("${application.kafka.output-topic}")
    private String topic;

    //TODO: implement to send processed order
    private final KafkaTemplate<String , OrderEventDto> kafkaTemplate;

    public void sendOrderEvent(final OrderEventDto orderEventDto) {

        log.info("Send event to orders topic: {}", orderEventDto);
        final ProducerRecord<String, OrderEventDto> record =
                new ProducerRecord<>(topic,
                        String.valueOf(orderEventDto.getUserId()),
                        orderEventDto);

        var future = kafkaTemplate.send(record);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Order: {} was sent to Kafka topic", orderEventDto);
            }
            else {
                log.error("Error while sending order event", ex);
            }
        });
    }
}