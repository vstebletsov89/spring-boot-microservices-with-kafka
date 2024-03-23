package ru.otus.hw.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.models.Order;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ItemMapper.class}
)
public interface OrderMapper {

     @Mappings({
             @Mapping(target = "items", source = "order.items"),
             @Mapping(target = "orderState", source = "order.state"),
             @Mapping(target = "orderNumber", source = "order.orderNumber")
     })
     OrderDto toDto(Order order);

}
