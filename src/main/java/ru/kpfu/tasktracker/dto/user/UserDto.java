package ru.kpfu.tasktracker.dto.user;

import ru.kpfu.tasktracker.dto.project.ProjectTitleDto;

import java.util.List;

public record UserDto(
        Long id,
        String name,
        List<ProjectTitleDto> projects
) {
}
