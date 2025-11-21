package ru.kpfu.tasktracker.dto.task;

public record KanbanCardDto (
        Long id,
        String title,
        String description,
        String color,
        Integer displayOrder
) {}
