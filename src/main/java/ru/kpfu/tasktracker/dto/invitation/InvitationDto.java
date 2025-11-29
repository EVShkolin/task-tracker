package ru.kpfu.tasktracker.dto.invitation;

import ru.kpfu.tasktracker.model.Status;

import java.time.Instant;

public record InvitationDto(
        Long id,
        Long userId,
        Long projectId,
        String projectTitle,
        Status status,
        Instant createdAt
) {}
