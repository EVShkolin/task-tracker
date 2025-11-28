package ru.kpfu.tasktracker.model;

import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Project {

    private Long id;

    private String title;

    private String description;

    private Instant createdAt;

    private List<ProjectMember> members = new ArrayList<>();

    private List<KanbanCard> cards = new ArrayList<>();

}
