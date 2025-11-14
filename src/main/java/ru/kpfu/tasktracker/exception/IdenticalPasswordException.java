package ru.kpfu.tasktracker.exception;

public class IdenticalPasswordException extends RuntimeException {

    public IdenticalPasswordException() {
        super("New password must be different from the current one");
    }

}
