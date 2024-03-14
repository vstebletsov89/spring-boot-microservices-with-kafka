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
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.feign.LibraryServiceProxy;
import ru.otus.hw.models.ResponseServerMessage;
import ru.otus.hw.services.BookService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка работы глобального обработчика ошибок")
@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureMockMvc
class RestExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookController bookController;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @MockBean
    private LibraryServiceProxy libraryServiceProxy;

    @DisplayName("должен обрабатывать исключение, когда книга не найдена")
    @Test
    void shouldHandleNotFoundExceptionForBook() throws Exception {
        when(bookService.findById(anyLong()))
                .thenThrow(new NotFoundException("Book with id 999 not found"));
        var expectedResponse = ResponseServerMessage.builder()
                .errorMessage("Book with id 999 not found")
                .stackTrace(null);

        mockMvc.perform(get("/api/v1/books/{id}", 999))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }


    @DisplayName("должен обрабатывать исключени внутренней ошибки сервера")
    @Test
    void shouldHandleAnyRuntimeException() throws Exception {
        when(bookService.findById(anyLong()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v1/books/{id}", 999))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(content()
                        .string(containsString("stackTrace\":\"java.lang.RuntimeException")));
    }
}