package ru.kpfu.tasktracker.dto.projectmember;

import ru.kpfu.tasktracker.model.Role;

public record MemberCreateDto(
        Long id,
        Long userId,
        Long projectId,
        Role role
) {}
