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
public class Comment {

    private Long id;

    private String content;

    private Instant createdAt;

    private ProjectMember author;

    private Task task;

}
