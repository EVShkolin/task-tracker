package ru.kpfu.tasktracker.dto.user;

public record UpdatePasswordRequest(Long userId, String newPassword, String confirmPassword) {}
