package ru.kpfu.tasktracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.dto.task.KanbanCardDto;
import ru.kpfu.tasktracker.exception.ObjectNotFoundException;
import ru.kpfu.tasktracker.mapper.KanbanCardMapper;
import ru.kpfu.tasktracker.model.KanbanCard;
import ru.kpfu.tasktracker.model.Project;
import ru.kpfu.tasktracker.repository.KanbanCardRepository;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class KanbanCardService {

    private final KanbanCardRepository cardRepository;
    private final KanbanCardMapper cardMapper;

    public List<KanbanCardDto> getAllByProjectId(Long projectId) {
        log.debug("IN KanbanCardService get all by project id {}", projectId);
        return cardRepository.getAllByProjectId(projectId).stream()
                .map(cardMapper::toDtoWithTasks)
                .toList();
    }

    public KanbanCardDto create(KanbanCardDto cardDto) {
        log.debug("IN KanbanCardService create {}", cardDto);
        KanbanCard card = cardMapper.fromDto(cardDto);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    public KanbanCardDto update(Long id, KanbanCardDto cardDto) {
        log.debug("IN KanbanCardService update {}", cardDto);
        KanbanCard card = cardRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("card", id));
        card.setTitle(cardDto.getTitle());
        card.setDescription(cardDto.getDescription());
        card.setColor(cardDto.getColor());
        return cardMapper.toDtoWithTasks(cardRepository.save(card));
    }

    public void delete(Long id) {
        log.debug("IN KanbanCardService delete {}", id);
        cardRepository.delete(id);
    }

    public void createDefaultCards(Project project) {
        log.debug("IN KanbanCardService create default cards");
        List<KanbanCard> defaultCards = Stream.of(
                KanbanCard.builder()
                        .title("No Status")
                        .project(project)
                        .displayOrder(0)
                        .build(),
                KanbanCard.builder()
                        .title("Todo")
                        .description("This item hasn't been started")
                        .project(project)
                        .color("#3fb950")
                        .displayOrder(1)
                        .build(),
                KanbanCard.builder()
                        .title("In Progress")
                        .description("This item is in progress")
                        .project(project)
                        .color("#f85149")
                        .displayOrder(2)
                        .build(),
                KanbanCard.builder()
                        .title("Done")
                        .description("This has been completed")
                        .project(project)
                        .color("#ab7df8")
                        .displayOrder(3)
                        .build()
        ).map(cardRepository::save).toList();
    }
}
