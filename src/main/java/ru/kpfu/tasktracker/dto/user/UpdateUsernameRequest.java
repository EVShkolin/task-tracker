package ru.kpfu.tasktracker.dto.user;

public record UpdateUsernameRequest(Long userId, String newUsername) {}
