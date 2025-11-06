package ru.kpfu.tasktracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Project {

    private Long id;

    private String title;

    private String description;

    private Instant createdAt;

    private List<ProjectMember> members;

    private List<KanbanCard> cards;

}
