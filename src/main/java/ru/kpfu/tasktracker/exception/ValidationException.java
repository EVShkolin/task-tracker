package ru.kpfu.tasktracker.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {

    private Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Validation failed with " + errors.size() + " error(s)");
        this.errors = errors;
    }

    public ValidationException(String error) {
        super(error);
    }

}
