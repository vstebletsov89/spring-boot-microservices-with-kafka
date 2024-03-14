package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.feign.LibraryServiceProxy;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final LibraryServiceProxy libraryServiceProxy;

    @Override
    public BookDto findById(long id) {
        return libraryServiceProxy.getBookById(id);
    }

    @Override
    public List<BookDto> findAll() {
        return libraryServiceProxy.getAllBooks();
    }

    @Override
    public BookDto create(BookCreateDto bookCreateDto) {
        return libraryServiceProxy.addBook(bookCreateDto);
    }

    @Override
    public BookDto update(BookUpdateDto bookUpdateDto) {
        return libraryServiceProxy.updateBook(bookUpdateDto);
    }

    @Override
    public void deleteById(long id) {
        libraryServiceProxy.deleteBook(id);
    }
}
