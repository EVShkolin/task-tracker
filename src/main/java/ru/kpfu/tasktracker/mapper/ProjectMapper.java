package ru.kpfu.tasktracker.mapper;

import ru.kpfu.tasktracker.dto.project.ProjectDto;
import ru.kpfu.tasktracker.model.Project;

import java.time.Instant;

public class ProjectMapper {

    public ProjectDto toDto(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getMembers(),
                project.getCards()
        );
    }

    public Project fromDto(ProjectDto dto) {
        return Project.builder()
                .title(dto.title())
                .description(dto.description())
                .createdAt(Instant.now())
                .build();
    }
}
