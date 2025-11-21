package ru.kpfu.tasktracker.util;

import ru.kpfu.tasktracker.model.*;

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

    public static ProjectMember convertToProjectMemberWithUser(ResultSet rs) throws SQLException {
        return new ProjectMember(
                rs.getLong("id"),
                Role.valueOf(rs.getString("role")),
                rs.getTimestamp("joined_at").toInstant(),
                null,
                convertToUser(rs),
                new ArrayList<>()
        );
    }

    public static ProjectMember convertToProjectMemberWithProject(ResultSet rs) throws SQLException {
        return new ProjectMember(
                rs.getLong("id"),
                Role.valueOf(rs.getString("role")),
                rs.getTimestamp("joined_at").toInstant(),
                convertToProject(rs),
                null,
                new ArrayList<>()
        );
    }

    public static Task convertToTask(ResultSet rs) throws SQLException {
        KanbanCard card = KanbanCard.builder()
                .id(rs.getLong("card_id"))
                .title(rs.getString("card_title"))
                .description(rs.getString("description"))
                .color(rs.getString("color"))
                .displayOrder(rs.getInt("display_order"))
                .tasks(new ArrayList<>())
                .build();

        Task task = Task.builder()
                .id(rs.getLong("task_id"))
                .title(rs.getString("task_title"))
                .content(rs.getString("content"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .updatedAt(rs.getTimestamp("updated_at") == null ?
                        null : rs.getTimestamp("updated_at").toInstant())
                .card(card)
                .build();
        card.getTasks().add(task);
        return task;
    }

}
