package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Payment;
import ru.otus.hw.models.TransactionType;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderNumber(String orderNumber);

    List<Payment> findPaymentByType(TransactionType transactionType);
}
