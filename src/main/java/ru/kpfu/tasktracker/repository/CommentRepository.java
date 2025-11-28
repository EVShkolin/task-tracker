package ru.kpfu.tasktracker.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.model.Comment;
import ru.kpfu.tasktracker.util.ResultSetConverter;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CommentRepository {

    private final DataSource dataSource;

    public List<Comment> getCommentsByTaskId(Long taskId) {
        String query = """
                SELECT  c.id AS comment_id,
                        c.content,
                        c.created_at,
                        pm.id AS member_id,
                        pm.role,
                        pm.joined_at,
                        u.id AS id,
                        u.username,
                        u.password_hash,
                        u.is_active,
                        u.registered_at,
                        u.is_admin
                FROM comments c
                JOIN project_members pm ON c.author_id = pm.id
                JOIN users u ON pm.user_id = u.id
                WHERE c.task_id = ?
                """;
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, taskId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Comment comment = ResultSetConverter.convertToComment(rs);
                comments.add(comment);
            }
            return comments;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Comment save(Comment comment) {
        String query = "INSERT INTO comments (content, author_id, task_id, created_at) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, comment.getContent());
            ps.setLong(2, comment.getAuthor().getId());
            ps.setLong(3, comment.getTask().getId());
            ps.setTimestamp(4, Timestamp.from(comment.getCreatedAt()));
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            comment.setId(generatedKeys.getLong("id"));
            return comment;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
