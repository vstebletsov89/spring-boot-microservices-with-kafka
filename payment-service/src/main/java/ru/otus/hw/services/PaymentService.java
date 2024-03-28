package ru.otus.hw.services;//package ru.otus.hw.services;

import ru.otus.hw.dto.OrderEventDto;

public interface PaymentService {

    void create(OrderEventDto orderEventDto);

    void executePayment(String orderNumber, long payerId);

    void cancel(String orderNumber);

}
