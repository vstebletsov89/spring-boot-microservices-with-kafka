package ru.otus.hw.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-14T17:44:56+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.8.1 (Amazon.com Inc.)"
)
@Component
public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public AuthorDto toDto(Author author) {
        if ( author == null ) {
            return null;
        }

        AuthorDto authorDto = new AuthorDto();

        authorDto.setId( author.getId() );
        authorDto.setFullName( author.getFullName() );

        return authorDto;
    }

    @Override
    public Author toModel(AuthorDto authorDto) {
        if ( authorDto == null ) {
            return null;
        }

        Author author = new Author();

        author.setId( authorDto.getId() );
        author.setFullName( authorDto.getFullName() );

        return author;
    }
}
