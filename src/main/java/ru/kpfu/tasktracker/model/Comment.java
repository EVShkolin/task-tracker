package ru.kpfu.tasktracker.model;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {

    private Long id;

    private String content;

    private Instant createdAt;

    private ProjectMember author;

    private Task task;

}
