package ru.kpfu.tasktracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.dto.project.ProjectCreateDto;
import ru.kpfu.tasktracker.dto.project.ProjectDto;
import ru.kpfu.tasktracker.dto.projectmember.MemberCreateDto;
import ru.kpfu.tasktracker.dto.projectmember.ProjectMemberDto;
import ru.kpfu.tasktracker.dto.task.KanbanCardDto;
import ru.kpfu.tasktracker.exception.ObjectNotFoundException;
import ru.kpfu.tasktracker.mapper.ProjectMapper;
import ru.kpfu.tasktracker.model.Project;
import ru.kpfu.tasktracker.model.ProjectMember;
import ru.kpfu.tasktracker.model.Role;
import ru.kpfu.tasktracker.repository.ProjectRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberService projectMemberService;
    private final KanbanCardService kanbanCardService;
    private final ProjectMapper projectMapper;

    public ProjectDto findById(Long id) {
        log.debug("IN ProjectService get project by id {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("project", id));
        ProjectDto projectDto = projectMapper.toDto(project);
        List<ProjectMemberDto> members = projectMemberService.findAllByProject(project);
        List<KanbanCardDto> cardDtos = kanbanCardService.getAllByProjectId(id);
        projectDto.setMembers(members);
        projectDto.setCards(cardDtos);
        return projectDto;
    }

    public ProjectDto save(ProjectCreateDto dto) {
        log.debug("IN ProjectService save project {}", dto);
        // todo use transaction
        Project project = projectMapper.fromDto(dto);
        project = projectRepository.save(project);

        MemberCreateDto memberCreateDto = new MemberCreateDto(null, dto.creatorId(), project.getId(), Role.CREATOR);
        projectMemberService.save(memberCreateDto);
        kanbanCardService.createDefaultCards(project);
        log.info("Created new project {}", project.getId());
        return projectMapper.toDto(project);
    }

    public void update(Long id, ProjectCreateDto dto) {
        log.debug("IN ProjectService update project {} {}", id, dto);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("project", id));
        project.setTitle(dto.title());
        project.setDescription(dto.description());
        projectRepository.save(project);
    }

    public void delete(Long id) {
        log.debug("IN ProjectService delete project {}", id);
        projectRepository.delete(id);
        log.info("Deleted project {}", id);
    }
}
