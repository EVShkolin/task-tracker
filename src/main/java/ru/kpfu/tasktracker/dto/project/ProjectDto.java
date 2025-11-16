package ru.kpfu.tasktracker.dto.project;

import ru.kpfu.tasktracker.model.KanbanCard;
import ru.kpfu.tasktracker.model.ProjectMember;

import java.util.List;

public record ProjectDto(
        Long id,
        String title,
        String description,
        List<ProjectMember> members,
        List<KanbanCard> cards) {}
