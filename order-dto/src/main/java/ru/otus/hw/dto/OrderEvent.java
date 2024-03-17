package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderEvent {

    private Long userId;
    private String state;
    private String orderNumber;
    private BigDecimal amount;
}
