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
public class User {

    private Long id;

    private String username;

    private String passwordHash;

    private boolean isAdmin;

    private boolean isActive;

    private Instant registeredAt;

    private List<ProjectMember> memberships;

}

