package ru.kpfu.tasktracker.dto.projectmember;

import ru.kpfu.tasktracker.model.Role;

public record ProjectMemberDto(
        Long id,
        String username,
        Role role
) {}
