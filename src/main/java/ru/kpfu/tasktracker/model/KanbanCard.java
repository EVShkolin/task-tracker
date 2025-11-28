package ru.kpfu.tasktracker.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KanbanCard {

    private Long id;

    private String title;

    private String description;

    private String color;

    private Integer displayOrder;

    private Project project;

    private List<Task> tasks = new ArrayList<>();

}
