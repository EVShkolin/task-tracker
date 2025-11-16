package ru.kpfu.tasktracker.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.model.Project;
import ru.kpfu.tasktracker.util.ResultSetConverter;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ProjectRepository {

    private final DataSource dataSource;

    public Optional<Project> findById(Long id) {
        String query = "SELECT * FROM projects WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(ResultSetConverter.convertToProject(rs)) :
                    Optional.empty();
        } catch (SQLException e) {
            log.info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Project save(Project project) {
        return project.getId() == null ? insert(project) : update(project);
    }

    private Project insert(Project project) {
        String query = """
                INSERT INTO projects (title, description, created_at)
                VALUES (?, ?, ?)
                """;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.getTitle());
            ps.setString(2, project.getDescription());
            ps.setTimestamp(3, Timestamp.from(project.getCreatedAt()));
            ps.executeQuery();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            project.setId(generatedKeys.getLong("id"));
            return project;
        } catch (SQLException e) {
            log.info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private Project update(Project project) {
        String query = "UPDATE projects SET title = ?, description = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, project.getTitle());
            ps.setString(2, project.getDescription());
            ps.setLong(3, project.getId());
            ps.executeUpdate();
            return project;
        } catch (SQLException e) {
            log.info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
