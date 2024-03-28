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
    private String processedOrdersTopic;

    @Value("${application.kafka.output-dlq-topic}")
    private String failedOrdersTopic;

    private final KafkaTemplate<String , OrderEventDto> kafkaTemplate;

    public void sendProcessedOrderEvent(OrderEventDto orderEventDto) {
        log.info("Send event: {} to {} topic", orderEventDto, processedOrdersTopic);
        final ProducerRecord<String, OrderEventDto> record =
                new ProducerRecord<>(processedOrdersTopic,
                        String.valueOf(orderEventDto.getUserId()),
                        orderEventDto);

        var future = kafkaTemplate.send(record);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Processed order: {} was sent to {} topic", orderEventDto, processedOrdersTopic);
            } else {
                log.error("Error while sending processed order event", ex);
            }
        });
    }

    public void sendFailedOrderEvent(OrderEventDto orderEventDto) {
        log.info("Send error event: {} to {} topic", orderEventDto, failedOrdersTopic);
        final ProducerRecord<String, OrderEventDto> record =
                new ProducerRecord<>(failedOrdersTopic,
                        String.valueOf(orderEventDto.getUserId()),
                        orderEventDto);

        var future = kafkaTemplate.send(record);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Failed order: {} was sent to {} topic", orderEventDto, failedOrdersTopic);
            } else {
                log.error("Error while sending failed order event", ex);
            }
        });
    }
}