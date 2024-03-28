package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.ItemDto;
import ru.otus.hw.models.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto toDto(Item item);

    Item toModel(ItemDto itemDto);
}
