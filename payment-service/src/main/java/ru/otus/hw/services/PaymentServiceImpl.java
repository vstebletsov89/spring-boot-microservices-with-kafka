package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.OrderEventDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.Payment;
import ru.otus.hw.models.TransactionType;
import ru.otus.hw.repositories.PaymentRepository;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    @Override
    public void create(OrderEventDto orderEventDto) {
        Payment payment = new Payment();
        payment.setType(TransactionType.CREATED);
        payment.setUserId(orderEventDto.getUserId());
        payment.setOrderNumber(orderEventDto.getOrderNumber());
        var currentTime = LocalDateTime.now();
        payment.setCreatedAt(currentTime);
        payment.setUpdatedAt(currentTime);
        paymentRepository.save(payment);
        log.info("Payment created {}", payment);
    }

    @Transactional
    @Override
    public void cancel(String orderNumber) {
        var payment = paymentRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() ->
                        new NotFoundException("Payment with order number '%s' not found".formatted(orderNumber)));

        payment.setType(TransactionType.REFUND);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        log.info("Payment canceled {}", payment);
    }

    @Transactional
    @Override
    public void executePayment(String orderNumber, long payerId) {
        var payment = paymentRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() ->
                        new NotFoundException("Payment with order number '%s' not found".formatted(orderNumber)));

        log.info("Execute payment for order {} and payer {}", orderNumber, payerId);
        payment.setType(TransactionType.PROCESSED);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        log.info("Payment {} successfully executed", payment);
    }
}
