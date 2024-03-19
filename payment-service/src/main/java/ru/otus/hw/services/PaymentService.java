package ru.otus.hw.services;//package ru.otus.hw.services;

import ru.otus.hw.dto.OrderEventDto;
import ru.otus.hw.models.Payment;
import ru.otus.hw.models.TransactionType;

import java.util.List;

public interface PaymentService {

    void addNewPaymentOperation(OrderEventDto orderEventDto);

    void updatePaymentStatus(String orderNumber, TransactionType transactionType);

    List<Payment> getRefundPayments();
}
