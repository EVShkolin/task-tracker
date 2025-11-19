package ru.kpfu.tasktracker.dto.projectmember;

import ru.kpfu.tasktracker.model.Role;

public record RoleUpdateRequest(
        Long memberId,
        Role role
) {
}
