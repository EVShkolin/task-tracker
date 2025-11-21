package ru.kpfu.tasktracker.controller.validator;

import ru.kpfu.tasktracker.exception.InvalidPathException;

public class PathValidator {

    public static Long getIdFromPath(String path) {
        if (path == null || path.equals("/")) {
            throw new InvalidPathException("The uri doesn't contain id of resource");
        }
        String[] parts = path.split("/");
        if (parts.length != 2 || !isNumeric(parts[1])) {
            throw new InvalidPathException("Incorrect path format");
        }
        return Long.valueOf(parts[1]);
    }

    public static boolean isValidAssignmentPath(String path) {
        String[] parts = path.split("/");
        if (parts.length != 4) { // .../{task_id}/assign/{member_id}
            return false;
        }
        return isNumeric(parts[1]) && isNumeric(parts[3]) && parts[2].equals("assign");
    }

    public static boolean isValidStatusUpdatePath(String path) {
        String[] parts = path.split("/");
        if (parts.length != 4) { // .../{task_id}/status/{member_id}
            return false;
        }
        return isNumeric(parts[1]) && isNumeric(parts[3]) && parts[2].equals("status");
    }

    public static boolean isValidUnassignPath(String path) {
        String[] parts = path.split("/");
        if (parts.length != 4) { // .../{task_id}/unassign/{member_id}
            return false;
        }
        return isNumeric(parts[1]) && isNumeric(parts[3]) && parts[2].equals("unassign");
    }

    private static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
