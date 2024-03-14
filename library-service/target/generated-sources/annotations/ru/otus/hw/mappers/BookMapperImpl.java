package ru.otus.hw.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-14T17:44:57+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.8.1 (Amazon.com Inc.)"
)
@Component
public class BookMapperImpl implements BookMapper {

    private final AuthorMapper authorMapper;
    private final GenreMapper genreMapper;

    @Autowired
    public BookMapperImpl(AuthorMapper authorMapper, GenreMapper genreMapper) {

        this.authorMapper = authorMapper;
        this.genreMapper = genreMapper;
    }

    @Override
    public BookDto toDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto bookDto = new BookDto();

        bookDto.setAuthorDto( authorMapper.toDto( book.getAuthor() ) );
        bookDto.setGenreDto( genreMapper.toDto( book.getGenre() ) );
        bookDto.setId( book.getId() );
        bookDto.setTitle( book.getTitle() );

        return bookDto;
    }

    @Override
    public Book toModel(BookUpdateDto bookUpdateDto, Author author, Genre genre) {
        if ( bookUpdateDto == null && author == null && genre == null ) {
            return null;
        }

        Book book = new Book();

        if ( bookUpdateDto != null ) {
            book.setId( bookUpdateDto.getId() );
            book.setTitle( bookUpdateDto.getTitle() );
        }
        book.setAuthor( author );
        book.setGenre( genre );

        return book;
    }

    @Override
    public Book toModel(BookCreateDto bookCreateDto, Author author, Genre genre) {
        if ( bookCreateDto == null && author == null && genre == null ) {
            return null;
        }

        Book book = new Book();

        if ( bookCreateDto != null ) {
            book.setTitle( bookCreateDto.getTitle() );
        }
        book.setAuthor( author );
        book.setGenre( genre );
        book.setId( null );

        return book;
    }

    @Override
    public Book toModel(BookDto bookDto, Author author, Genre genre) {
        if ( bookDto == null && author == null && genre == null ) {
            return null;
        }

        Book book = new Book();

        if ( bookDto != null ) {
            book.setId( bookDto.getId() );
            book.setTitle( bookDto.getTitle() );
        }
        book.setAuthor( author );
        book.setGenre( genre );

        return book;
    }
}
