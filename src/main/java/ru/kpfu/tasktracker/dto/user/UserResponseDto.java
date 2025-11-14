package ru.kpfu.tasktracker.dto.user;

import ru.kpfu.tasktracker.model.ProjectMember;

import java.util.List;

public record UserResponseDto(Long id, String username, List<ProjectMember> memberships) {}
