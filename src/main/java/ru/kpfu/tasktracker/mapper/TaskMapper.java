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
                new ArrayList<>(),
                new ArrayList<>()
        );
    }


    public Task fromDto(TaskDto dto) {
        KanbanCard card = KanbanCard.builder()
                .id(dto.getCard().getId())
                .title(dto.getCard().getTitle())
                .description(dto.getCard().getDescription())
                .color(dto.getCard().getColor())
                .displayOrder(dto.getCard().getDisplayOrder())
                .tasks(new ArrayList<>())
                .build();

        Task task = Task.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .card(card)
                .updatedAt(Instant.now())
                .build();
        card.getTasks().add(task);
        return task;
    }

}
