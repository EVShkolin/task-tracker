package ru.kpfu.tasktracker.mapper;

import ru.kpfu.tasktracker.dto.task.KanbanCardDto;
import ru.kpfu.tasktracker.dto.task.TaskDto;
import ru.kpfu.tasktracker.model.KanbanCard;
import ru.kpfu.tasktracker.model.Project;
import ru.kpfu.tasktracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public class KanbanCardMapper {

    public KanbanCardDto toDtoWithTasks(KanbanCard kanbanCard) {
        List<Task> tasks = kanbanCard.getTasks();
        List<TaskDto> taskDtos = tasks.stream()
                .map(t -> new TaskDto(
                        t.getId(),
                        t.getTitle(),
                        t.getContent(),
                        t.getCreatedAt(),
                        null,
                        new ArrayList<>(),
                        new ArrayList<>()
                )).toList();

        return new KanbanCardDto(
                kanbanCard.getId(),
                kanbanCard.getTitle(),
                kanbanCard.getDescription(),
                kanbanCard.getColor(),
                kanbanCard.getDisplayOrder(),
                kanbanCard.getProject().getId(),
                taskDtos
        );
    }

    public KanbanCardDto toDto(KanbanCard kanbanCard) {
        return new KanbanCardDto(
                kanbanCard.getId(),
                kanbanCard.getTitle(),
                kanbanCard.getDescription(),
                kanbanCard.getColor(),
                kanbanCard.getDisplayOrder(),
                kanbanCard.getProject().getId(),
                new ArrayList<>()
        );
    }

    public KanbanCard fromDto(KanbanCardDto kanbanCardDto) {
        return KanbanCard.builder()
                .title(kanbanCardDto.getTitle())
                .description(kanbanCardDto.getDescription())
                .color(kanbanCardDto.getColor())
                .displayOrder(kanbanCardDto.getDisplayOrder())
                .project(Project.builder()
                        .id(kanbanCardDto.getProjectId())
                        .build())
                .build();
    }

}
