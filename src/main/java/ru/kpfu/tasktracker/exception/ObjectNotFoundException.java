package ru.kpfu.tasktracker.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String object, Long id) {
        super("Could not find " + object + " with id " + id);
    }

    public ObjectNotFoundException(String object, String name) {
        super("Could not find " + object + " " + name);
    }

}
