package ru.kpfu.tasktracker.dto.project;

import ru.kpfu.tasktracker.dto.projectmember.ProjectMemberDto;
import ru.kpfu.tasktracker.model.KanbanCard;

import java.util.List;

public record ProjectDto(
        Long id,
        String title,
        String description,
        List<ProjectMemberDto> members,
        List<KanbanCard> cards) {}
