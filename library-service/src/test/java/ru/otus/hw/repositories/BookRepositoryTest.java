package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.hw.models.Book;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Репозиторий для работы с книгами")
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("получить все книги списком")
    @Test
    void shouldReturnAllBooks() {
        var actualBooks = bookRepository.findAll();

        assertEquals(3, actualBooks.size());
        assertEquals(Book.class, actualBooks.get(0).getClass());
    }

    @DisplayName("получить одну книгу по id")
    @Test
    void shouldReturnOneBook() {
        Optional<Book> actualBook = bookRepository.findById(1L);

        assertThat(actualBook).isPresent();
        assertEquals(1, actualBook.get().getId());
        assertEquals("TestBookTitle_1", actualBook.get().getTitle());
        assertEquals(1, actualBook.get().getAuthor().getId());
        assertEquals(1, actualBook.get().getGenre().getId());
    }
}