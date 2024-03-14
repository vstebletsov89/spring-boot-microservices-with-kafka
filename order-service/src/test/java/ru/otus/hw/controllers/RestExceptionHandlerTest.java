package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.GenreMapperImpl;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.ResponseServerMessage;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.BookServiceImpl;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка работы глобального обработчика ошибок")
@SpringBootTest(classes = {
        BookController.class,
        BookServiceImpl.class,
        RestExceptionHandler.class,
        BookMapperImpl.class,
        AuthorMapperImpl.class,
        GenreMapperImpl.class
})
@AutoConfigureWebMvc
@AutoConfigureMockMvc
class RestExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private BookController bookController;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @DisplayName("должен обрабатывать исключение, когда книга не найдена")
    @Test
    void shouldHandleNotFoundExceptionForBook() throws Exception {
        doReturn(Optional.empty()).when(bookRepository).findById(anyLong());
        var expectedResponse = ResponseServerMessage.builder()
                .errorMessage("Book with id 999 not found")
                .stackTrace(null);

        mockMvc.perform(get("/api/v1/books/{id}", 999))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(bookRepository, times(1)).findById(anyLong());
    }

    @DisplayName("должен обрабатывать исключение, когда автор не найден")
    @Test
    void shouldHandleNotFoundExceptionForAuthor() throws Exception {
        doReturn(Optional.empty()).when(authorRepository).findById(anyLong());
        doReturn(Optional.empty()).when(genreRepository).findById(anyLong());
        var expectedResponse = ResponseServerMessage.builder()
                .errorMessage("Author with id 999 not found")
                .stackTrace(null);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new BookCreateDto("newTitle",
                                        999L,
                                        999L))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(authorRepository, times(1)).findById(anyLong());
    }

    @DisplayName("должен обрабатывать исключение, когда жанр не найден")
    @Test
    void shouldHandleNotFoundExceptionForGenre() throws Exception {
        doReturn(Optional.of(new Author())).when(authorRepository).findById(anyLong());
        doReturn(Optional.empty()).when(genreRepository).findById(anyLong());
        var expectedResponse = ResponseServerMessage.builder()
                .errorMessage("Genre with id 999 not found")
                .stackTrace(null);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new BookCreateDto("newTitle",
                                        999L,
                                        999L))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(authorRepository, times(1)).findById(anyLong());
    }

    @DisplayName("должен обрабатывать исключени внутренней ошибки сервера")
    @Test
    void shouldHandleAnyRuntimeException() throws Exception {
        when(bookRepository.findById(anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v1/books/{id}", 999))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content()
                        .string(containsString("stackTrace\":\"java.lang.RuntimeException")));

    }
}