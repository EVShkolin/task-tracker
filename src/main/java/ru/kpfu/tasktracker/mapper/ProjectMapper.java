package ru.kpfu.tasktracker.mapper;

import ru.kpfu.tasktracker.dto.project.ProjectCreateDto;
import ru.kpfu.tasktracker.dto.project.ProjectDto;
import ru.kpfu.tasktracker.model.Project;

import java.time.Instant;
import java.util.ArrayList;

public class ProjectMapper {

    public ProjectDto toDto(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public Project fromDto(ProjectCreateDto dto) {
        return Project.builder()
                .title(dto.title())
                .description(dto.description())
                .createdAt(Instant.now())
                .members(new ArrayList<>())
                .cards(new ArrayList<>())
                .build();
    }
}
