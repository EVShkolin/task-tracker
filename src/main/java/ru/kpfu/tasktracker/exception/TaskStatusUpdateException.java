package ru.kpfu.tasktracker.exception;

public class TaskStatusUpdateException extends RuntimeException {

    public TaskStatusUpdateException(Long taskId, Long cardId) {
        super("Task " + taskId + " and kanban card " + cardId + " aren't in the same project");
    }

}
