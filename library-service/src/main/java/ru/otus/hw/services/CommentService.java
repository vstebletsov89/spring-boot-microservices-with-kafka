package ru.otus.hw.services;

import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {
    CommentDto findById(long id);

    List<CommentDto> findAllByBookId(long id);

    CommentDto create(CommentCreateDto book);

    CommentDto update(CommentUpdateDto book);

    void deleteById(long id);
}
