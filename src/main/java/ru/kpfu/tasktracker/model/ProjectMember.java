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
public class ProjectMember {

    private Long id;

    private Role role;

    private Instant joinedAt;

    private Project project;

    private User user;

    private List<Task> tasks;

}
