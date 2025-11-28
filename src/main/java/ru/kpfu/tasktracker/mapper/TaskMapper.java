package ru.kpfu.tasktracker.mapper;

import ru.kpfu.tasktracker.dto.task.KanbanCardDto;
import ru.kpfu.tasktracker.dto.task.TaskDto;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;
import ru.kpfu.tasktracker.model.KanbanCard;
import ru.kpfu.tasktracker.model.Task;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TaskMapper {

    public TaskDto toDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getCreatedAt(),
                new KanbanCardDto(
                        task.getCard().getId(),
                        task.getCard().getTitle(),
                        task.getCard().getDescription(),
                        task.getCard().getColor(),
                        task.getCard().getDisplayOrder(),
                        null,
                        new ArrayList<>()
                ),
                new ArrayList<>()
        );
    }

    public TaskDto toDto(Task task, List<UserProfileDto> assignees) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getCreatedAt(),
                new KanbanCardDto(
                        task.getCard().getId(),
                        task.getCard().getTitle(),
                        task.getCard().getDescription(),
                        task.getCard().getColor(),
                        task.getCard().getDisplayOrder(),
                        null,
                        new ArrayList<>()
                ),
                assignees
        );
    }

    public Task fromDto(TaskDto dto) {
        KanbanCard card = KanbanCard.builder()
                .id(dto.card().getId())
                .title(dto.card().getTitle())
                .description(dto.card().getDescription())
                .color(dto.card().getColor())
                .displayOrder(dto.card().getDisplayOrder())
                .tasks(new ArrayList<>())
                .build();

        Task task = Task.builder()
                .title(dto.title())
                .content(dto.content())
                .card(card)
                .updatedAt(Instant.now())
                .build();
        card.getTasks().add(task);
        return task;
    }

}
