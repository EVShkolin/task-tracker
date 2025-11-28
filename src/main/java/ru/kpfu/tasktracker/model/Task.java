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
public class Task {

    private Long id;

    private String title;

    private String content;

    private Instant createdAt;

    private Instant updatedAt;

    private KanbanCard card;

    private List<ProjectMember> assignees = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

}
