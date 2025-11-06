package ru.kpfu.tasktracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KanbanCard {

    private Long id;

    private String title;

    private String description;

    private String color;

    private Integer displayOrder;

    private Project project;

    private List<Task> tasks;

}
