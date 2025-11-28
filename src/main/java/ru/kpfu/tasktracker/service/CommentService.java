package ru.kpfu.tasktracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.dto.comment.CommentCreateDto;
import ru.kpfu.tasktracker.dto.comment.CommentDto;
import ru.kpfu.tasktracker.mapper.CommentMapper;
import ru.kpfu.tasktracker.model.Comment;
import ru.kpfu.tasktracker.repository.CommentRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public List<CommentDto> getAllCommentsByTaskId(Long taskId) {
        log.debug("IN CommentService get all comments by taskId {}", taskId);
        return commentRepository.getCommentsByTaskId(taskId).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public CommentDto save(CommentCreateDto commentDto) {
        log.debug("IN CommentService save comment {}", commentDto);
        Comment comment = commentMapper.fromDto(commentDto);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }
}
