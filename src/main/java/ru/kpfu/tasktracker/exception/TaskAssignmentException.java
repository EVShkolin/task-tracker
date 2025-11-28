package ru.kpfu.tasktracker.exception;

public class TaskAssignmentException extends RuntimeException {

    public TaskAssignmentException(Long taskId, Long memberId) {
        super("Task " + taskId + " and assigned member " + memberId + " aren't in the same project");
    }

}
