package ru.kpfu.tasktracker.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.dto.projectmember.MemberCreateDto;
import ru.kpfu.tasktracker.model.Project;
import ru.kpfu.tasktracker.model.ProjectMember;
import ru.kpfu.tasktracker.model.Role;
import ru.kpfu.tasktracker.model.User;
import ru.kpfu.tasktracker.util.ResultSetConverter;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ProjectMemberRepository {

    private final DataSource dataSource;

    public List<ProjectMember> findByUser(User user) {
        String query = """
        SELECT p.*, pm.id, pm.role, pm.joined_at
        FROM project_members pm
        JOIN users u ON pm.user_id = u.id AND pm.user_id = ?
        JOIN projects p ON pm.project_id = p.id
        """;
        List<ProjectMember> projectMembers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProjectMember projectMember = ResultSetConverter.convertToProjectMemberWithProject(rs);
                projectMember.setUser(user);
                projectMembers.add(projectMember);
            }
            return projectMembers;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<ProjectMember> findByProject(Project project) {
        String query = """
        SELECT u.*, pm.id, pm.role, pm.joined_at
        FROM project_members pm
        JOIN projects p ON pm.project_id = p.id AND pm.project_id = ?
        JOIN users u ON pm.user_id = u.id
        """;
        List<ProjectMember> projectMembers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, project.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProjectMember projectMember = ResultSetConverter.convertToProjectMemberWithUser(rs);
                projectMember.setProject(project);
                projectMembers.add(projectMember);
            }
            return projectMembers;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<ProjectMember> findAllByTaskId(Long taskId) {
        String query = """
                SELECT * FROM project_members pm
                JOIN members_tasks mt ON pm.id = mt.project_member_id
                AND mt.task_id = ?
                JOIN users u ON pm.user_id = u.id
                """;
        List<ProjectMember> projectMembers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, taskId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProjectMember projectMember = ResultSetConverter.convertToProjectMemberWithUser(rs);
                projectMembers.add(projectMember);
            }
            return projectMembers;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void save(Long userId, Long projectId, String role) {
        String query = "INSERT INTO project_members (user_id, project_id, role) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, userId);
            ps.setLong(2, projectId);
            ps.setString(3, role);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateRole(Long id, String role) {
        String query = "UPDATE project_members SET role = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, role);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(Long id) {
        String query = "DELETE FROM project_members WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


}
