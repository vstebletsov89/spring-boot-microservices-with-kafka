package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private String customerNumber;

    private String orderState;

    private List<ItemDto> items;

//    private BigDecimal cost; //TODO: add it?
}
