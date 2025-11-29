package ru.kpfu.tasktracker.controller.validator;

import ru.kpfu.tasktracker.dto.invitation.InvitationDto;
import ru.kpfu.tasktracker.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

public class InvitationValidator {

    public void validate(InvitationDto invitation) {
        Map<String, String> errors = new HashMap<String, String>();

        if (invitation.userId() == null) {
            errors.put("userId", "userId is required");
        }
        if (invitation.projectId() == null) {
            errors.put("projectId", "projectId is required");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

}
