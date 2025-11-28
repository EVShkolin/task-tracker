package ru.kpfu.tasktracker.mapper;

import lombok.RequiredArgsConstructor;
import ru.kpfu.tasktracker.dto.project.ProjectTitleDto;
import ru.kpfu.tasktracker.dto.user.UserCreateDto;
import ru.kpfu.tasktracker.dto.user.UserDto;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;
import ru.kpfu.tasktracker.model.User;

import java.time.Instant;
import java.util.ArrayList;

@RequiredArgsConstructor
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getMemberships().stream()
                        .map(m -> new ProjectTitleDto(
                                m.getProject().getId(),
                                m.getProject().getTitle()
                        ))
                        .toList()
        );
    }

    public UserProfileDto toProfileDto(User user) {
        return new UserProfileDto(
                user.getId(),
                user.getUsername()
        );
    }

    public User fromDto(UserCreateDto dto) {
        return User.builder()
                .username(dto.username())
                .isAdmin(false)
                .isActive(true)
                .registeredAt(Instant.now())
                .memberships(new ArrayList<>())
                .build();
    }

}
