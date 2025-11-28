package ru.kpfu.tasktracker.dto.project;

public record ProjectCreateDto(
        Long creatorId,
        String title,
        String description
) {
}
