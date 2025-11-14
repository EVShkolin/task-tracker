package ru.kpfu.tasktracker.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super("User with name " + username + " already exists");
    }

}
