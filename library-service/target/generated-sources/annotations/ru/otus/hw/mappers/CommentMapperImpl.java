package ru.otus.hw.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-14T17:44:57+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.8.1 (Amazon.com Inc.)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentDto toDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDto commentDto = new CommentDto();

        commentDto.setBookId( commentBookId( comment ) );
        commentDto.setId( comment.getId() );
        commentDto.setText( comment.getText() );

        return commentDto;
    }

    @Override
    public Comment toModel(CommentDto commentDto, Book book) {
        if ( commentDto == null && book == null ) {
            return null;
        }

        Comment comment = new Comment();

        if ( commentDto != null ) {
            comment.setId( commentDto.getId() );
            comment.setText( commentDto.getText() );
        }
        comment.setBook( book );

        return comment;
    }

    @Override
    public Comment toModel(CommentCreateDto commentCreateDto, Book book) {
        if ( commentCreateDto == null && book == null ) {
            return null;
        }

        Comment comment = new Comment();

        if ( commentCreateDto != null ) {
            comment.setText( commentCreateDto.getText() );
        }
        comment.setBook( book );
        comment.setId( null );

        return comment;
    }

    @Override
    public Comment toModel(CommentUpdateDto commentUpdateDto, Book book) {
        if ( commentUpdateDto == null && book == null ) {
            return null;
        }

        Comment comment = new Comment();

        if ( commentUpdateDto != null ) {
            comment.setId( commentUpdateDto.getId() );
            comment.setText( commentUpdateDto.getText() );
        }
        comment.setBook( book );

        return comment;
    }

    private Long commentBookId(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        Book book = comment.getBook();
        if ( book == null ) {
            return null;
        }
        Long id = book.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
