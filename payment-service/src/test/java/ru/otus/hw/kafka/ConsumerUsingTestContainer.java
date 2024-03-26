package ru.otus.hw.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
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
    private KafkaTemplate<String, Object> kafkaTemplate;


    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers",
                KAFKA_CONTAINER::getBootstrapServers);
    }

    @Test
    void shouldHandleOrderEvent() {
        var event = new OrderEventDto(1L,
                OrderState.CREATED.toString(),
                "a0ab87ac-ad94-4fbd-aeec-05091393520e",
                BigDecimal.valueOf(99999.999));

        kafkaTemplate.send("orders", String.valueOf(event.getUserId()), event);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    //TODO: update to check payment was created
                    var newPayment =
                            paymentRepository.findByOrderNumber(event.getOrderNumber());

                    assertThat(newPayment).isPresent();
                    assertEquals(1L, newPayment.get().getId());
                    assertEquals(TransactionType.CREATED, newPayment.get().getType());
                    assertEquals(event.getOrderNumber(), newPayment.get().getOrderNumber());
                    assertThat(newPayment.get().getAmount())
                            .isEqualTo(BigDecimal.valueOf(99999.999));
                });
    }
}
