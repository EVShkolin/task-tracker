package ru.kpfu.tasktracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.dto.user.UserCreateDto;
import ru.kpfu.tasktracker.dto.user.UserDto;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;
import ru.kpfu.tasktracker.exception.IdenticalPasswordException;
import ru.kpfu.tasktracker.exception.ObjectNotFoundException;
import ru.kpfu.tasktracker.exception.UsernameAlreadyExistsException;
import ru.kpfu.tasktracker.mapper.UserMapper;
import ru.kpfu.tasktracker.model.ProjectMember;
import ru.kpfu.tasktracker.model.User;
import ru.kpfu.tasktracker.repository.UserRepository;
import ru.kpfu.tasktracker.security.BCryptPasswordEncoder;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProjectMemberService projectMemberService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder;

    public UserProfileDto findByUsername(String username) {
        log.debug("IN UserService find by username {}", username);
        return userRepository.findByUsername(username)
                .map(userMapper::toProfileDto)
                .orElseThrow(() -> new ObjectNotFoundException("user", username));
    }

    public UserDto findByUsernameWithProjects(String username) {
        log.debug("IN UserService find by username with projects {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("user", username));
        List<ProjectMember> memberships = projectMemberService.findAllByUser(user);
        user.setMemberships(memberships);
        return userMapper.toDto(user);
    }

    public UserProfileDto save(UserCreateDto userCreateDto) {
        log.debug("IN UserService save user {}", userCreateDto);
        checkUsername(userCreateDto.username());

        User user = userMapper.fromDto(userCreateDto);
        String passwordHash = encoder.encode(userCreateDto.password());
        user.setPasswordHash(passwordHash);
        user = userRepository.save(user);
        log.info("Saved new user with id {}", user.getId());
        return userMapper.toProfileDto(user);
    }

    public String changeUsername(Long userId, String username) {
        log.debug("IN UserService change username for user {}", userId);
        checkUsername(username);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        user.setUsername(username);
        user = userRepository.save(user);
        return user.getUsername();
    }

    private void checkUsername(String username) {
        if (userRepository.existsByUsername(username))
            throw new UsernameAlreadyExistsException(username);
    }

    public void changePassword(Long userId, String password) {
        log.debug("IN UserService change password for user {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));

        if (encoder.matches(password, user.getPasswordHash()))
            throw new IdenticalPasswordException();

        String passwordHash = encoder.encode(password);
        user.setPasswordHash(passwordHash);
        userRepository.save(user);
        log.info("Changed password for user {}", userId);
    }

    public boolean authenticate(UserCreateDto dto) {
        log.debug("IN UserService authenticate");
        User user = userRepository.findByUsername(dto.username())
                .orElse(null);
        if (user == null || !user.getIsActive()) {
            return false;
        }

        return encoder.matches(dto.password(), user.getPasswordHash());
    }

    public void softDelete(Long userId) {
        log.debug("IN UserRepository soft delete user {}", userId);
        userRepository.softDeleteById(userId);
        log.info("User {} deactivated account", userId);
    }

    public void hardDelete(Long userId) {
        log.debug("IN UserRepository hard delete user {}", userId);
        userRepository.hardDeleteById(userId);
        log.info("Account with id {} has been deleted", userId);
    }

}
