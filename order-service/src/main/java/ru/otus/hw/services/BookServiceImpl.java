package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        return bookMapper.toDto(
                bookRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id))));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public BookDto create(BookCreateDto bookCreateDto) {
        return addBook(bookCreateDto);
    }

    @Transactional
    @Override
    public BookDto update(BookUpdateDto bookUpdateDto) {
        return updateBook(bookUpdateDto);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private BookDto updateBook(BookUpdateDto bookUpdateDto) {
        var updatedBook =
                bookRepository.findById(bookUpdateDto.getId())
                        .orElseThrow(() ->
                                new NotFoundException("Book with id %d not found".formatted(bookUpdateDto.getId()))
                        );
        var author =
                authorRepository.findById(bookUpdateDto.getAuthorId())
                        .orElseThrow(() -> new NotFoundException("Author with id %d not found"
                                .formatted(bookUpdateDto.getAuthorId())
                        ));
        var genre =
                genreRepository.findById(bookUpdateDto.getGenreId())
                        .orElseThrow(() -> new NotFoundException("Genre with id %d not found"
                                .formatted(bookUpdateDto.getGenreId())
                        ));
        var newBook = bookMapper.toModel(bookUpdateDto, author, genre);
        return bookMapper.toDto(bookRepository.save(newBook));
    }

    private BookDto addBook(BookCreateDto bookCreateDto) {
        var author =
                authorRepository.findById(bookCreateDto.getAuthorId())
                        .orElseThrow(() -> new NotFoundException("Author with id %d not found"
                                .formatted(bookCreateDto.getAuthorId())
                        ));
        var genre =
                genreRepository.findById(bookCreateDto.getGenreId())
                        .orElseThrow(() -> new NotFoundException("Genre with id %d not found"
                                .formatted(bookCreateDto.getGenreId())
                        ));
        var newBook = bookMapper.toModel(bookCreateDto, author, genre);
        return bookMapper.toDto(bookRepository.save(newBook));
    }
}
