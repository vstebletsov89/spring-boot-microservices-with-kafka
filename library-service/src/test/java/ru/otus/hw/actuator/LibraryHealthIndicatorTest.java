package ru.otus.hw.actuator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {LibraryHealthIndicator.class})
class LibraryHealthIndicatorTest {

    @Autowired
    private LibraryHealthIndicator libraryHealthIndicator;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @DisplayName("должен возвращать UP status")
    @Test
    void shouldReturnUpStatus() {
        doReturn(List.of(new Author())).when(authorService).findAll();
        doReturn(List.of(new Genre())).when(genreService).findAll();
        var result = libraryHealthIndicator.health();

        assertEquals(Status.UP, result.getStatus());
        assertEquals("UP {message=Сервис в порядке!}", result.toString());
    }

    @DisplayName("должен возвращать DOWN status")
    @Test
    void shouldReturnDownStatus() {
        doReturn(Collections.emptyList()).when(authorService).findAll();
        doReturn(List.of(new Genre())).when(genreService).findAll();
        var result = libraryHealthIndicator.health();

        assertEquals(Status.DOWN, result.getStatus());
        assertEquals("DOWN {message=Данные в базе повреждены!}", result.toString());
    }

    @DisplayName("должен возвращать DOWN status и exception stacktrace")
    @Test
    void shouldReturnDownStatusAndStackTrace() {
        when(authorService.findAll()).thenThrow(new RuntimeException());
        doReturn(List.of(new Genre())).when(genreService).findAll();
        var result = libraryHealthIndicator.health();

        assertEquals(Status.DOWN, result.getStatus());
        assertThat(result.toString())
                .contains("DOWN {message=Ошибка обращения к базе: java.lang.RuntimeException");
    }
}