package ru.kpfu.tasktracker.mapper;

import ru.kpfu.tasktracker.dto.invitation.InvitationDto;
import ru.kpfu.tasktracker.model.Invitation;
import ru.kpfu.tasktracker.model.Project;
import ru.kpfu.tasktracker.model.Status;

import java.time.Instant;

public class InvitationMapper {

    public InvitationDto toDto(Invitation invitation) {
        return new InvitationDto(
                invitation.getId(),
                invitation.getUserId(),
                invitation.getProject().getId(),
                invitation.getProject().getTitle(),
                invitation.getStatus(),
                invitation.getCreatedAt()
        );
    }

    public Invitation fromDto(InvitationDto dto) {
        return new Invitation(
                dto.id(),
                dto.userId(),
                Project.builder()
                        .id(dto.projectId())
                        .build(),
                Status.PENDING,
                Instant.now(),
                Instant.now()
        );
    }

}
