package ru.kpfu.tasktracker.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kpfu.tasktracker.dto.comment.CommentDto;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDto {
    private Long id;
    private String title;
    private String content;
    private Instant createdAt;
    private KanbanCardDto card;
    private List<UserProfileDto> assignees;
    private List<CommentDto> comments;
}


