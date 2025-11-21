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
public class ProjectMember {

    private Long id;

    private Role role;

    private Instant joinedAt;

    private Project project;

    private User user;

    private List<Task> tasks = new ArrayList<>();

}
