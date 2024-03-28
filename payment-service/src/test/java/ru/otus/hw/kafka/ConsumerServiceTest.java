package ru.otus.hw.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.OrderEventDto;
import ru.otus.hw.dto.OrderState;
import ru.otus.hw.services.PaymentService;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1,
        brokerProperties =
                { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class ConsumerServiceTest {

    @Autowired
    private ConsumerService consumerService;

    @MockBean
    private ProducerService producerService;

    @MockBean
    private PaymentService paymentService;

    @Test
    void shouldConsumeNewOrder() {
        var event = new OrderEventDto(1L,
                OrderState.CREATED.toString(),
                "new_order_number",
                BigDecimal.valueOf(99999.999));

        consumerService.consume(event, () -> { });

        verify(producerService, times(1)).sendProcessedOrderEvent(event);
        verify(paymentService, times(1)).create(event);
        verify(paymentService, times(1))
                .executePayment(event.getOrderNumber(), event.getUserId());
    }

    @Test
    void shouldConsumeCanceledOrder() {
        var event = new OrderEventDto(1L,
                OrderState.CANCELED.toString(),
                "new_order_number",
                BigDecimal.valueOf(99999.999));

        consumerService.consume(event, () -> { });

        verify(producerService, times(1)).sendProcessedOrderEvent(event);
        verify(paymentService, times(1)).cancel(event.getOrderNumber());
    }

    @Test
    void shouldSendInvalidOrderToDlq() {
        var event = new OrderEventDto(1L,
                OrderState.SHIPPED.toString(),
                "new_order_number",
                BigDecimal.valueOf(99999.999));

        consumerService.consume(event, () -> { });

        verify(producerService, times(1)).sendFailedOrderEvent(event);
    }
}