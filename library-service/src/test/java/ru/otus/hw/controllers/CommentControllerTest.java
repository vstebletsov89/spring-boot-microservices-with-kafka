package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.services.CommentService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка работы контроллера комментариев")
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    private static List<CommentDto> expectedCommentsDto = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @BeforeAll
    static void setExpectedAuthors() {
        expectedCommentsDto = List.of(
                new CommentDto(1L, "TestCommentText_1",1L),
                new CommentDto(2L, "TestCommentText_2", 1L),
                new CommentDto(3L, "TestCommentText_3", 1L)
        );
    }

    @DisplayName("должен загружать список всех комментариев для книги")
    @Test
    void shouldReturnAllCommentsByBookId() throws Exception {
        var bookId = 1L;
        doReturn(expectedCommentsDto).when(commentService).findAllByBookId(bookId);

        mockMvc.perform(get("/api/v1/comments/book/{id}", bookId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedCommentsDto)));

        verify(commentService, times(1)).findAllByBookId(bookId);
    }

    @DisplayName("должен загружать комментарий")
    @Test
    void shouldReturnCommentById() throws Exception {
        doReturn(expectedCommentsDto.get(0)).when(commentService).findById(anyLong());

        mockMvc.perform(get("/api/v1/comments/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedCommentsDto.get(0))));

        verify(commentService, times(1)).findById(anyLong());
    }

    @DisplayName("должен добавить комментарий")
    @Test
    void shouldAddComment() throws Exception {
        CommentCreateDto commentCreateDto = new CommentCreateDto(
                "NewComment",
                3L);
        CommentDto expectedComment = new CommentDto(1L, "NewComment", 3L);
        doReturn(expectedComment).when(commentService).create(any(CommentCreateDto.class));

        mockMvc.perform(post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedComment)));

        verify(commentService, times(1)).create(commentCreateDto);
    }

    @DisplayName("должен обновить комментарий")
    @Test
    void shouldUpdateComment() throws Exception {
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto(
                1L,
                "NewComment",
                3L);
        CommentDto expectedComment = new CommentDto(1L, "NewComment", 3L);
        doReturn(expectedComment).when(commentService).update(any(CommentUpdateDto.class));

        mockMvc.perform(put("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentUpdateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedComment)));

        verify(commentService, times(1)).update(commentUpdateDto);
    }
}