package ru.kpfu.tasktracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Invitation {

    private Long id;

    private Long userId;

    private Project project;

    private Status status;

    private Instant createdAt;

    private Instant updatedAt;
}
