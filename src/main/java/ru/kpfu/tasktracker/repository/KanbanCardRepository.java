package ru.kpfu.tasktracker.repository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.model.KanbanCard;
import ru.kpfu.tasktracker.util.ResultSetConverter;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class KanbanCardRepository {

    private final DataSource dataSource;
    private final TaskRepository taskRepository;

    public List<KanbanCard> getAllByProjectId(Long projectId) {
        String query = """
                SELECT DISTINCT  kc.id AS card_id,
                        kc.title AS card_title,
                        kc.description,
                        kc.project_id,
                        kc.color,
                        kc.display_order
                FROM
                kanban_cards kc LEFT JOIN tasks t ON kc.id = t.kanban_card_id
                WHERE project_id = ?
                """;
        List<KanbanCard> kanbanCards = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, projectId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                KanbanCard card = ResultSetConverter.convertToCard(rs);
                card.setTasks(taskRepository.findByCardId(card.getId()));
                kanbanCards.add(card);
            }
            return kanbanCards;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<KanbanCard> findById(Long id) {
        String query = "SELECT * FROM kanban_cards WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(ResultSetConverter.convertToCard(rs)) : Optional.empty();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public KanbanCard save(KanbanCard kanbanCard) {
        return kanbanCard.getId() == null ? insert(kanbanCard) : update(kanbanCard);
    }

    private KanbanCard insert(KanbanCard kanbanCard) {
        String query = """
                INSERT INTO kanban_cards (title, description, project_id, color, display_order)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, kanbanCard.getTitle());
            ps.setString(2, kanbanCard.getDescription());
            ps.setLong(3, kanbanCard.getProject().getId());
            ps.setString(4, kanbanCard.getColor());
            ps.setInt(5, kanbanCard.getDisplayOrder());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            kanbanCard.setId(generatedKeys.getLong("id"));
            return kanbanCard;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private KanbanCard update(KanbanCard kanbanCard) {
        String query = "UPDATE kanban_cards SET title = ?, description = ?, color = ?, display_order = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, kanbanCard.getTitle());
            ps.setString(2, kanbanCard.getDescription());
            ps.setString(3, kanbanCard.getColor());
            ps.setInt(4, kanbanCard.getDisplayOrder());
            ps.setLong(5, kanbanCard.getId());
            ps.executeUpdate();
            return kanbanCard;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(Long id) {
        String query = "DELETE FROM kanban_cards WHERE id = ?";
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
