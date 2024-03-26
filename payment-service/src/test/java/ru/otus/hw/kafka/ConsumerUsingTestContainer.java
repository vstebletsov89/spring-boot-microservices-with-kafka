package ru.otus.hw.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.otus.hw.dto.OrderEventDto;
import ru.otus.hw.dto.OrderState;
import ru.otus.hw.models.TransactionType;
import ru.otus.hw.repositories.PaymentRepository;

import java.math.BigDecimal;
import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.kafka.consumer.auto-offset-reset=earliest"
        }
)
@Testcontainers
class ConsumerUsingTestContainer {

    @Container
    static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.4.0")
    ).withKraft();

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TestProducerOrders testProducerOrders;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers",
                KAFKA_CONTAINER::getBootstrapServers);
    }

    @Test
    void shouldProcessNewOrderEvent() throws InterruptedException {
        var event = new OrderEventDto(1L,
                OrderState.CREATED.toString(),
                "a0ab87ac-ad94-4fbd-aeec-05091393520e",
                BigDecimal.valueOf(99999.99));

        Thread.sleep(5000);
        testProducerOrders.sendOrderEvent(event);

        await()
                .pollInterval(Duration.ofSeconds(5))
                .atMost(15, SECONDS)
                .untilAsserted(() -> {
                    var newPayment =
                            paymentRepository.findByOrderNumber(event.getOrderNumber());

                    assertThat(newPayment).isPresent();
                    assertEquals(TransactionType.PROCESSED, newPayment.get().getType());
                    assertEquals(event.getOrderNumber(), newPayment.get().getOrderNumber());
                    assertEquals(event.getAmount(), newPayment.get().getAmount());
                });
    }

    @Test
    void shouldCancelOrder() throws InterruptedException {
        var newOrder = new OrderEventDto(1L,
                OrderState.CREATED.toString(),
                "5865a38d-0ff3-4fee-9ee5-d963f6d2959e",
                BigDecimal.valueOf(50000.99));

        var cancelOrder = new OrderEventDto(1L,
                OrderState.CANCELED.toString(),
                "5865a38d-0ff3-4fee-9ee5-d963f6d2959e",
                BigDecimal.valueOf(0));

        Thread.sleep(5000);
        // create order
        testProducerOrders.sendOrderEvent(newOrder);
        testProducerOrders.sendOrderEvent(cancelOrder);

        await()
                .pollInterval(Duration.ofSeconds(5))
                .atMost(15, SECONDS)
                .untilAsserted(() -> {
                    var canceledPayment =
                            paymentRepository.findByOrderNumber(cancelOrder.getOrderNumber());

                    assertThat(canceledPayment).isPresent();
                    assertEquals(TransactionType.REFUND, canceledPayment.get().getType());
                    assertEquals(cancelOrder.getOrderNumber(), canceledPayment.get().getOrderNumber());
                    assertEquals(newOrder.getAmount(), canceledPayment.get().getAmount());
                });
    }
}
