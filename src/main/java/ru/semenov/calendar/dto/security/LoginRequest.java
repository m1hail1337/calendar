package ru.semenov.calendar.dto.security;

public record LoginRequest(
    String login,
    String password
) { }
