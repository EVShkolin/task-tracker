package ru.kpfu.tasktracker.util;

import ru.kpfu.tasktracker.model.Project;
import ru.kpfu.tasktracker.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ResultSetConverter {

    public static User convertToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getBoolean("is_admin"),
                rs.getBoolean("is_active"),
                rs.getTimestamp("registered_at").toInstant(),
                new ArrayList<>()
        );
    }

    public static Project convertToProject(ResultSet rs) throws SQLException {
        return new Project(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getTimestamp("created_at").toInstant(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

}
