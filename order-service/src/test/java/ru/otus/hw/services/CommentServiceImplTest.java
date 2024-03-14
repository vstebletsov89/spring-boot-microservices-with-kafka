package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.mappers.CommentMapperImpl;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@DisplayName("Проверка работы сервиса коментариев")
@SpringBootTest(classes = {CommentServiceImpl.class, CommentMapperImpl.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentServiceImplTest {

    private static List<Comment> expectedComments = new ArrayList<>();

    private static List<CommentDto> expectedCommentsDto = new ArrayList<>();

    private static Book expectedBook = new Book(1L, "testBook", null, null);

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @BeforeAll
    void setExpectedComments() {
        expectedComments = List.of(
                new Comment(1L, "TestComment1", expectedBook),
                new Comment(2L, "TestComment2", expectedBook),
                new Comment(3L, "TestComment3", expectedBook)
        );
        expectedCommentsDto =
                expectedComments.stream()
                        .map(commentMapper::toDto)
                        .toList();
    }

    @DisplayName("должен загружать комент по id")
    @Test
    void shouldReturnCorrectCommentById() {
        long commentId = 2L;
        int commentPos = 1;
        doReturn(Optional.of(expectedComments.get(commentPos))).when(commentRepository).findById(commentId);
        var actualComment = commentService.findById(commentId);

        assertThat(actualComment)
                .usingRecursiveComparison()
                .isEqualTo(expectedCommentsDto.get(commentPos));
    }

    @DisplayName("должен выбрасывать исключение для неверного id")
    @Test
    void shouldReturnExceptionForInvalidId() {
        doReturn(Optional.empty()).when(commentRepository).findById(99L);
        var exception = assertThrows(NotFoundException.class,
                () -> commentService.findById(99L));

        assertEquals("Comment with id 99 not found", exception.getMessage());
    }

    @DisplayName("должен загружать список всех коментариев для книги")
    @Test
    void shouldReturnCorrectCommentsByBookId() {
        doReturn(Optional.of(expectedBook)).when(bookRepository).findById(1L);
        doReturn(expectedComments).when(commentRepository).findAllByBookId(1L);
        var actualComments = commentService.findAllByBookId(1L);

        assertEquals(3, actualComments.size());
        actualComments.forEach(System.out::println);
        assertEquals(expectedCommentsDto, actualComments);
    }

    @DisplayName("должен сохранять новый коммент")
    @Test
    void shouldSaveNewComment() {
        var newComment = new CommentCreateDto("newComment", 1L);
        var expectedComment = new Comment(null, "newComment", expectedBook);
        var expectedCommentDto = commentMapper.toDto(
                expectedComment);
        doReturn(Optional.of(expectedBook)).when(bookRepository).findById(1L);
        doReturn(expectedComment).when(commentRepository).save(any());
        var actualComment = commentService.create(newComment);

        assertThat(actualComment).isEqualTo(expectedCommentDto);
    }

    @DisplayName("должен обновить коммент")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment(1L, "updatedComment", expectedBook);
        var expectedCommentDto = commentMapper.toDto(
                expectedComment);
        doReturn(Optional.of(expectedComment)).when(commentRepository).findById(1L);
        doReturn(Optional.of(expectedBook)).when(bookRepository).findById(1L);
        doReturn(expectedComment).when(commentRepository).save(expectedComment);
        var actualComment = commentService.update(
            new CommentUpdateDto(1L, expectedComment.getText(), expectedComment.getBook().getId())
        );

        assertThat(actualComment).isEqualTo(expectedCommentDto);
    }

}