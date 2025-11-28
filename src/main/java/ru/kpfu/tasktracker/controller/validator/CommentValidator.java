package ru.kpfu.tasktracker.controller.validator;

import ru.kpfu.tasktracker.dto.comment.CommentCreateDto;
import ru.kpfu.tasktracker.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

public class CommentValidator {

    public void validateComment(CommentCreateDto comment) {
        Map<String, String> errors = new HashMap<>();
        if (comment.taskId() == null) {
            errors.put("taskId", "Task id cannot be null");
        }
        if (comment.authorId() == null) {
            errors.put("authorId", "Author id cannot be null");
        }
        if (comment.content() == null || comment.content().trim().isEmpty()) {
            errors.put("content", "Content cannot be empty");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

}
