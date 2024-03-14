package ru.otus.hw.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-14T17:44:57+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.8.1 (Amazon.com Inc.)"
)
@Component
public class GenreMapperImpl implements GenreMapper {

    @Override
    public GenreDto toDto(Genre genre) {
        if ( genre == null ) {
            return null;
        }

        GenreDto genreDto = new GenreDto();

        genreDto.setId( genre.getId() );
        genreDto.setName( genre.getName() );

        return genreDto;
    }

    @Override
    public Genre toModel(GenreDto genreDto) {
        if ( genreDto == null ) {
            return null;
        }

        Genre genre = new Genre();

        genre.setId( genreDto.getId() );
        genre.setName( genreDto.getName() );

        return genre;
    }
}
