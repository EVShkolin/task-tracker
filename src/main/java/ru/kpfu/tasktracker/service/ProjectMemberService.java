package ru.kpfu.tasktracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.dto.projectmember.MemberCreateDto;
import ru.kpfu.tasktracker.dto.projectmember.RoleUpdateRequest;
import ru.kpfu.tasktracker.model.Project;
import ru.kpfu.tasktracker.model.ProjectMember;
import ru.kpfu.tasktracker.model.Role;
import ru.kpfu.tasktracker.model.User;
import ru.kpfu.tasktracker.repository.ProjectMemberRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;

    public List<ProjectMember> findAllByUser(User user) {
        log.debug("IN ProjectMemberService find all by user {}", user.getId());
        return projectMemberRepository.findByUser(user);
    }

    public List<ProjectMember> findAllByProject(Project project) {
        log.debug("IN ProjectMemberService find all by project {}", project.getId());
        return projectMemberRepository.findByProject(project);
    }

    public void save(MemberCreateDto memberDto) {
        log.debug("IN ProjectMemberService save {}", memberDto);
        String role = memberDto.role() == null ? Role.VIEWER.toString() : memberDto.role().toString();
        projectMemberRepository.save(memberDto.userId(), memberDto.projectId(), role);
    }

    public void updateRole(Long id, RoleUpdateRequest request) {
        log.debug("IN ProjectMemberService update role {}", request);
        projectMemberRepository.updateRole(id, request.role().toString());
    }

    public void delete(Long id) {
        log.debug("IN ProjectMemberService delete {}", id);
        projectMemberRepository.delete(id);
    }

}
