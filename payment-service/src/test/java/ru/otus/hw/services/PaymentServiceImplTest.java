package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dto.OrderEventDto;
import ru.otus.hw.dto.OrderState;
import ru.otus.hw.models.Payment;
import ru.otus.hw.repositories.PaymentRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Проверка работы сервиса оплаты")
@SpringBootTest(classes = {PaymentServiceImpl.class})
class PaymentServiceImplTest {

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private PaymentRepository paymentRepository;

    @DisplayName("должен создавать новый счет на оплату")
    @Test
    void shouldCreatePayment() {
        doReturn(null)
                .when(paymentRepository)
                .save(any());
        var event = new OrderEventDto(
               1L,
                OrderState.CREATED.toString(),
                "order_number",
                BigDecimal.valueOf(999.99));
        paymentService.create(event);

        verify(paymentRepository, times(1)).save(any());
    }

    @DisplayName("должен отменять оплату")
    @Test
    void shouldCancelPayment() {
        doReturn(Optional.of(new Payment()))
                .when(paymentRepository)
                .findByOrderNumber(anyString());
        paymentService.cancel("order_number");

        verify(paymentRepository, times(1)).save(any());
    }

    @DisplayName("должен проводить оплату")
    @Test
    void shouldExecutePayment() {
        doReturn(Optional.of(new Payment()))
                .when(paymentRepository)
                .findByOrderNumber(anyString());
        paymentService.executePayment("order_number", 1L);

        verify(paymentRepository, times(1)).save(any());
    }
}