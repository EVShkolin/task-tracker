package ru.kpfu.tasktracker.controller.validator;

import ru.kpfu.tasktracker.dto.projectmember.RoleUpdateRequest;
import ru.kpfu.tasktracker.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

public class ProjectMemberValidator {

    public void validateRoleUpdateRequest(RoleUpdateRequest roleUpdateRequest) {
        Map<String, String> errors = new HashMap<>();

        if (roleUpdateRequest.memberId() == null) {
            errors.put("memberId", "memberId is required");
        }
        if (roleUpdateRequest.role() == null) {
            errors.put("role", "role is required");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

}
