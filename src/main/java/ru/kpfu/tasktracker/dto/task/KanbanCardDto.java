package ru.kpfu.tasktracker.dto.task;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KanbanCardDto {

    private Long id;

    private String title;

    private String description;

    private String color;

    private Integer displayOrder;

    private Long projectId;

    private List<TaskDto> tasks;

}
