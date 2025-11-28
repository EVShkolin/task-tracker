package ru.kpfu.tasktracker.mapper;

import ru.kpfu.tasktracker.dto.projectmember.ProjectMemberDto;
import ru.kpfu.tasktracker.model.ProjectMember;

public class ProjectMemberMapper {

    public ProjectMemberDto toDto(ProjectMember projectMember) {
        return new ProjectMemberDto(
                projectMember.getId(),
                projectMember.getUser().getUsername(),
                projectMember.getRole()
        );
    }

}
