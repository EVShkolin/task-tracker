package ru.kpfu.tasktracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.dto.project.ProjectDto;
import ru.kpfu.tasktracker.exception.ObjectNotFoundException;
import ru.kpfu.tasktracker.mapper.ProjectMapper;
import ru.kpfu.tasktracker.model.Project;
import ru.kpfu.tasktracker.repository.ProjectRepository;

@Slf4j
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectDto findById(Long id) {
        log.debug("IN ProjectService get project by id {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("project", id));
        return projectMapper.toDto(project);
    }

    public ProjectDto save(ProjectDto dto) {
        log.debug("IN ProjectService save project {}", dto);
        Project project = projectMapper.fromDto(dto);
        project = projectRepository.save(project);
        return projectMapper.toDto(project);
    }
}
