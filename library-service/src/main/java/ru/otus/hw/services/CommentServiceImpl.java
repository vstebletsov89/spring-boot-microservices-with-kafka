package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.exceptions.InvalidStateException;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    public CommentDto findById(long id) {
        return commentMapper.toDto(
                commentRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Comment with id %d not found".formatted(id))));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(long id) {
        var book =
                bookRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Book with id %d not found"
                                .formatted(id)
                        ));
        return commentRepository.findAllByBookId(book.getId())
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentDto create(CommentCreateDto commentCreateDto) {
        var book =
                bookRepository.findById(commentCreateDto.getBookId())
                        .orElseThrow(() -> new NotFoundException("Book with id %d not found"
                                .formatted(commentCreateDto.getBookId())
                        ));
        var newComment = commentMapper.toModel(commentCreateDto, book);
        return commentMapper.toDto(
                commentRepository.save(newComment));
    }

    @Transactional
    @Override
    public CommentDto update(CommentUpdateDto commentUpdateDto) {

        var updatedComment =
                commentRepository.findById(commentUpdateDto.getId())
                        .orElseThrow(() -> new NotFoundException("Comment with id %d not found"
                                .formatted(commentUpdateDto.getId())));

        if (!Objects.equals(updatedComment.getBook().getId(), commentUpdateDto.getBookId())) {
            throw new InvalidStateException("Cannot change comment for another book");
        }
        updatedComment.setText(commentUpdateDto.getText());
        return commentMapper.toDto(commentRepository.save(updatedComment));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

}
