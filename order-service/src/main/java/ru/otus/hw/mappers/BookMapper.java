package ru.otus.hw.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {AuthorMapper.class, GenreMapper.class}
)
public interface BookMapper {

     @Mappings({
             @Mapping(target = "authorDto", source = "book.author"),
             @Mapping(target = "genreDto", source = "book.genre")
     })
     BookDto toDto(Book book);

     @Mapping(target = "id", source = "bookUpdateDto.id")
     Book toModel(BookUpdateDto bookUpdateDto, Author author, Genre genre);

     @Mapping(target = "id", expression = "java(null)")
     Book toModel(BookCreateDto bookCreateDto, Author author, Genre genre);

     @Mapping(target = "id", source = "bookDto.id")
     Book toModel(BookDto bookDto, Author author, Genre genre);
}
