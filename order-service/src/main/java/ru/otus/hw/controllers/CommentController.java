package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/v1/comments/book/{id}")
    public List<CommentDto> getCommentsByBookId(@PathVariable("id") long id) {
        return commentService.findAllByBookId(id);
    }

    @GetMapping("/api/v1/comments/{id}")
    public CommentDto getCommentById(@PathVariable("id") long id) {
        return commentService.findById(id);
    }

    @PostMapping("/api/v1/comments")
    public CommentDto addComment(@RequestBody @Valid CommentCreateDto commentCreateDto) {
        log.info(commentCreateDto.toString());
        return commentService.create(commentCreateDto);
    }

    @PutMapping("/api/v1/comments")
    public CommentDto updateComment(@RequestBody @Valid CommentUpdateDto commentUpdateDto) {
        log.info(commentUpdateDto.toString());
        return commentService.update(commentUpdateDto);
    }
}
