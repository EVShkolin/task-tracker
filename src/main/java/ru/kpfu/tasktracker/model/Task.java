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
public class Task {

    private Long id;

    private String title;

    private String content;

    private Instant createdAt;

    private Instant updatedAt;

    private KanbanCard card;

    private List<ProjectMember> assignees;

    private List<Comment> comments;

}
