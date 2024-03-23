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

    @Value("${application.kafka.topic}")
    private String ordersTopic;

    private final KafkaTemplate<String , OrderEventDto> kafkaTemplate;

    public void sendOrderEvent(final OrderEventDto orderEventDto) {

        log.info("Send event: {} to {} topic", orderEventDto, ordersTopic);
        final ProducerRecord<String, OrderEventDto> record =
                new ProducerRecord<>(ordersTopic,
                        String.valueOf(orderEventDto.getUserId()),
                        orderEventDto);

        var future = kafkaTemplate.send(record);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Order: {} was sent to {} topic", orderEventDto, ordersTopic);
            } else {
                log.error("Error while sending order event", ex);
            }
        });
    }
}