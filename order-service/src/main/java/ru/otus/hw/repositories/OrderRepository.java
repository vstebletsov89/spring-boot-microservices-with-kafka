package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.dto.OrderState;
import ru.otus.hw.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(value = "order-entity-graph")
    Optional<Order> findOrderByOrderNumber(String orderNumber);

    @EntityGraph(value = "order-entity-graph")
    List<Order> findOrderByState(OrderState state);
}
