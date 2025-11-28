package ru.kpfu.tasktracker.dto.comment;

public record CommentCreateDto(
        Long taskId,
        Long authorId,
        String content
) {}
