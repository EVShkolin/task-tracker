package ru.kpfu.tasktracker.dto.task;

import ru.kpfu.tasktracker.dto.user.UserProfileDto;

import java.time.Instant;
import java.util.List;

public record TaskDto (
        Long id,
        String title,
        String content,
        Instant createdAt,
        KanbanCardDto card,
        List<UserProfileDto> assignees
) {}
