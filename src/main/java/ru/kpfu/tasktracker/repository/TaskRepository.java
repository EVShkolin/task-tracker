package ru.kpfu.tasktracker.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.model.Task;
import ru.kpfu.tasktracker.util.ResultSetConverter;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TaskRepository {

    private final DataSource dataSource;

    public Optional<Task> findById(Long id) {
        String query = """
            SELECT  t.id AS task_id,
                    t.title AS task_title,
                    t.content,
                    t.created_at,
                    t.updated_at,
                    t.kanban_card_id,
                    kc.id AS card_id,
                    kc.title AS card_title,
                    kc.description,
                    kc.color,
                    kc.display_order,
                    kc.project_id
            FROM tasks t
            JOIN kanban_cards kc ON t.kanban_card_id = kc.id
            WHERE t.id = ?
        """;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(ResultSetConverter.convertToTask(rs)) : Optional.empty();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void addAssignee(Long taskId, Long memberId) {
        String query = "INSERT INTO members_tasks (project_member_id, task_id) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, memberId);
            ps.setLong(2, taskId);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Task save(Task task) {
        return task.getId() == null ? insert(task) : update(task);
    }

    private Task insert(Task task) {
        String query = """
                INSERT INTO tasks (title, content, kanban_card_id, updated_at)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getContent());
            ps.setLong(3, task.getCard().getId());
            ps.setTimestamp(4, Timestamp.from(task.getUpdatedAt()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();

            task.setId(rs.getLong("id"));
            task.setCreatedAt(rs.getTimestamp("created_at").toInstant());
            return task;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private Task update(Task task) {
        String query = """
                UPDATE tasks SET title = ?, content = ?, kanban_card_id = ?, updated_at = ?
                WHERE id = ?
                """;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getContent());
            ps.setLong(3, task.getCard().getId());
            ps.setTimestamp(4, Timestamp.from(task.getUpdatedAt()));
            ps.setLong(5, task.getId());
            ps.executeUpdate();
            return task;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateStatus(Long taskId, Long cardId) {
        String query = "UPDATE tasks SET kanban_card_id = ?, updated_at = NOW() WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, cardId);
            ps.setLong(2, taskId);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(Long id) {
        String query = "DELETE FROM tasks WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void removeAssignee(Long taskId, Long memberId) {
        String query = "DELETE FROM members_tasks WHERE task_id = ? AND project_member_id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, taskId);
            ps.setLong(2, memberId);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean taskAndMemberInSameProject(Long taskId, Long memberId) {
        String query = """
                SELECT * FROM
                tasks t JOIN kanban_cards kc ON t.id = kc.id
                AND t.id = ?
                JOIN project_members pm ON pm.project_id = kc.project_id
                WHERE pm.id = ?
                """;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, taskId);
            ps.setLong(2, memberId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean taskAndCardInSameProject(Long taskId, Long cardId) {
        String query = """
                SELECT COUNT (DISTINCT project_id) AS count FROM (
                    SELECT project_id
                    FROM tasks t JOIN kanban_cards kc ON t.kanban_card_id = kc.id AND t.id = ?
                    UNION
                    SELECT project_id
                    FROM kanban_cards WHERE id = ?) AS project_ids;
                """;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, taskId);
            ps.setLong(2, cardId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("count") == 1;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
