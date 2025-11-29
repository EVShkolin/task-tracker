package ru.kpfu.tasktracker.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.model.Invitation;
import ru.kpfu.tasktracker.util.ResultSetConverter;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class InvitationRepository {

    private final DataSource dataSource;

    public List<Invitation> findAllByUserId(Long userId) {
        String query = """
                SELECT  i.id AS invitation_id,
                        i.user_id,
                        i.status,
                        i.created_at,
                        i.updated_at,
                        p.id AS project_id,
                        p.title
                FROM invitations i
                JOIN projects p ON i.project_id = p.id
                WHERE status = 'PENDING' AND i.user_id = ?
                """;
        List<Invitation> invitations = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invitation invitation = ResultSetConverter.convertToInvitation(rs);
                invitations.add(invitation);
            }
            return invitations;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<Invitation> findById(Long id) {
        String query = """
                SELECT  i.id AS invitation_id,
                        i.user_id,
                        i.status,
                        i.created_at,
                        i.updated_at,
                        p.id AS project_id,
                        p.title
                FROM invitations i
                JOIN projects p ON i.project_id = p.id
                WHERE i.id = ? AND status = 'PENDING'
                """;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            return rs.next() ? Optional.of(ResultSetConverter.convertToInvitation(rs)) : Optional.empty();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Invitation save(Invitation invitation) {
        return invitation.getId() == null ? insert(invitation) : update(invitation);
    }

    private Invitation insert(Invitation invitation) {
        String query = "INSERT INTO invitations (user_id, project_id, status) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, invitation.getUserId());
            ps.setLong(2, invitation.getProject().getId());
            ps.setString(3, invitation.getStatus().name());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            invitation.setId(generatedKeys.getLong(1));
            return invitation;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private Invitation update(Invitation invitation) {
        String query = "UPDATE invitations SET status = ?, updated_at = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, invitation.getStatus().name());
            ps.setTimestamp(2, Timestamp.from(invitation.getUpdatedAt()));
            ps.setLong(3, invitation.getId());
            ps.executeUpdate();
            return invitation;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
