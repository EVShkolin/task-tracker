package ru.kpfu.tasktracker.mapper;

import ru.kpfu.tasktracker.dto.comment.CommentCreateDto;
import ru.kpfu.tasktracker.dto.comment.CommentDto;
import ru.kpfu.tasktracker.model.Comment;
import ru.kpfu.tasktracker.model.ProjectMember;
import ru.kpfu.tasktracker.model.Task;
import ru.kpfu.tasktracker.model.User;

import java.time.Instant;

public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getUser().getUsername(),
                comment.getCreatedAt()
        );
    }

    public Comment fromDto(CommentCreateDto dto) {
        return Comment.builder()
                .content(dto.content())
                .createdAt(Instant.now())
                .author(
                        ProjectMember.builder()
                                .id(dto.authorId())
                                .user(new User())
                                .build())
                .task(
                        Task.builder()
                                .id(dto.taskId())
                                .build())
                .build();
    }
}
