package ru.kpfu.tasktracker.controller.validator;

import ru.kpfu.tasktracker.dto.user.UpdatePasswordRequest;
import ru.kpfu.tasktracker.dto.user.UpdateUsernameRequest;
import ru.kpfu.tasktracker.dto.user.UserCreateDto;
import ru.kpfu.tasktracker.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;


public class UserValidator {

    public void validateNewUser(UserCreateDto user) {
        Map<String, String> errors = new HashMap<>();
        if (user.username() == null || user.username().length() < 5) {
            errors.put("username", "Username must be at least 5 characters long");
        }
        if (user.password() == null || user.password().length() < 5) {
            errors.put("password", "Password must be at least 5 characters long");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateUpdateUsernameRequest(UpdateUsernameRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (request.newUsername() == null || request.newUsername().length() < 5) {
            errors.put("username", "Username must be at least 5 characters long");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateUpdatePasswordRequest(UpdatePasswordRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (request.newPassword() == null || request.newPassword().length() < 5) {
            errors.put("newPassword", "Password must be at least 5 characters long");
        }
        if (request.confirmPassword() == null || !request.confirmPassword().equals(request.newPassword())) {
            errors.put("confirmPassword", "Passwords doesn't match");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

}
