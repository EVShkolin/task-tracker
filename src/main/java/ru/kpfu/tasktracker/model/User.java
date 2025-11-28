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
public class User {

    private Long id;

    private String username;

    private String passwordHash;

    private Boolean isAdmin;

    private Boolean isActive;

    private Instant registeredAt;

    private List<ProjectMember> memberships = new ArrayList<>();

}

