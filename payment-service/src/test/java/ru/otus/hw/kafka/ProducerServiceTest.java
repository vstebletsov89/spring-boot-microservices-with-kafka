package ru.otus.hw.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.OrderEventDto;
import ru.otus.hw.dto.OrderState;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1,
        brokerProperties =
                { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class ProducerServiceTest {

    @Autowired
    private ProducerService producerService;

    @Autowired
    private TestConsumerProcessedOrders testConsumerProcessedOrders;

    @Autowired
    private TestConsumerFailedOrders testConsumerFailedOrders;

    @Test
    void shouldSendProcessedOrderEventToKafka() throws InterruptedException {
        var event = new OrderEventDto(1L,
                OrderState.COMPLETED.toString(),
                "order_number",
                BigDecimal.valueOf(99999.999));

        // send event and consume
        producerService.sendProcessedOrderEvent(event);

        boolean eventConsumed = testConsumerProcessedOrders.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(eventConsumed);
        var consumedEvent = testConsumerProcessedOrders.getConsumedEvent();
        assertEquals(event, consumedEvent);
    }

    @Test
    void shouldSendFailedOrderEventToKafka() throws InterruptedException {
        var event = new OrderEventDto(null,
                null,
                "invalid_number",
                BigDecimal.valueOf(-1));

        // send event and consume
        producerService.sendFailedOrderEvent(event);

        boolean eventConsumed = testConsumerFailedOrders.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(eventConsumed);
        var consumedEvent = testConsumerFailedOrders.getConsumedEvent();
        assertEquals(event, consumedEvent);
    }
}