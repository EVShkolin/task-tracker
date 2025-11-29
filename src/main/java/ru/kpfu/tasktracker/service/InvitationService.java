package ru.kpfu.tasktracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.dto.invitation.InvitationDto;
import ru.kpfu.tasktracker.dto.projectmember.MemberCreateDto;
import ru.kpfu.tasktracker.exception.ObjectNotFoundException;
import ru.kpfu.tasktracker.mapper.InvitationMapper;
import ru.kpfu.tasktracker.model.Invitation;
import ru.kpfu.tasktracker.model.Role;
import ru.kpfu.tasktracker.model.Status;
import ru.kpfu.tasktracker.repository.InvitationRepository;

import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;
    private final ProjectMemberService projectMemberService;

    public List<InvitationDto> findAllByUserId(Long userId) {
        log.debug("IN InvitationService find all by user id {}", userId);
        return invitationRepository.findAllByUserId(userId).stream()
                .map(invitationMapper::toDto)
                .toList();
    }

    public InvitationDto save(InvitationDto invitationDto) {
        log.debug("IN InvitationService save invitation {}", invitationDto);
        Invitation invitation = invitationMapper.fromDto(invitationDto);
        invitation = invitationRepository.save(invitation);
        return invitationMapper.toDto(invitation);
    }

    public InvitationDto updateStatus(Long id, InvitationDto invitationDto) {
        log.debug("IN InvitationService update status for invitation {} to {}", id, invitationDto.status());
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("invitation", id));
        Status newStatus = invitationDto.status();

        if (newStatus.equals(Status.ACCEPTED)) {
            MemberCreateDto newMember = new MemberCreateDto(
                    null,
                    invitation.getUserId(),
                    invitation.getProject().getId(),
                    Role.VIEWER);
            projectMemberService.save(newMember);
            invitation.setStatus(Status.ACCEPTED);
        } else if (newStatus.equals(Status.DECLINED)) {
            invitation.setStatus(Status.DECLINED);
        } else {
            throw new RuntimeException("Wrong invitation status");
        }
        invitation.setUpdatedAt(Instant.now());
        invitation = invitationRepository.save(invitation);
        return invitationMapper.toDto(invitation);
    }

}
