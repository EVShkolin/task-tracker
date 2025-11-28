package ru.kpfu.tasktracker.dto.project;

import lombok.*;
import ru.kpfu.tasktracker.dto.projectmember.ProjectMemberDto;
import ru.kpfu.tasktracker.dto.task.KanbanCardDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectDto {

    private Long id;

    private String title;

    private String description;

    private List<ProjectMemberDto> members;

    private List<KanbanCardDto> cards;

}
