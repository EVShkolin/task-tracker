package ru.kpfu.tasktracker.mapper;

import lombok.RequiredArgsConstructor;
import ru.kpfu.tasktracker.dto.user.UserCreateDto;
import ru.kpfu.tasktracker.dto.user.UserResponseDto;
import ru.kpfu.tasktracker.model.User;

import java.time.Instant;
import java.util.ArrayList;

@RequiredArgsConstructor
public class UserMapper {

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getMemberships()
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
