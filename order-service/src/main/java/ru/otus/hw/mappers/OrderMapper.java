package ru.otus.hw.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.otus.hw.dto.OrderCreateDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.models.Order;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ItemMapper.class}
)
public interface OrderMapper {

     @Mappings({
             @Mapping(target = "items", source = "order.items"),
     })
     OrderDto toDto(Order order);

     @Mapping(target = "id", expression = "java(null)")
     Order toModel(OrderCreateDto orderCreateDto);

}
