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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    @Override
    public void addNewPaymentOperation(OrderEventDto orderEventDto)
    {
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
    public void updatePaymentStatus(String orderNumber, TransactionType transactionType) {
        var payment = paymentRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() ->
                        new NotFoundException("Payment with order number '%s' not found".formatted(orderNumber)));
        payment.setType(transactionType);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        log.info("Payment updated {}", payment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Payment> getRefundPayments() {
        log.info("Getting refund payments");
        return paymentRepository.findPaymentByType(TransactionType.REFUND);
    }
}
