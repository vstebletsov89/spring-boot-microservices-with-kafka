package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "bookId", source = "book.id")
    CommentDto toDto(Comment comment);

    @Mapping(target = "id", source = "commentDto.id")
    Comment toModel(CommentDto commentDto, Book book);

    @Mappings({
            @Mapping(target = "book", source = "book"),
            @Mapping(target = "id", expression = "java(null)")
    })
    Comment toModel(CommentCreateDto commentCreateDto, Book book);

    @Mappings({
            @Mapping(target = "book", source = "book"),
            @Mapping(target = "id", source = "commentUpdateDto.id")
    })
    Comment toModel(CommentUpdateDto commentUpdateDto, Book book);
}
