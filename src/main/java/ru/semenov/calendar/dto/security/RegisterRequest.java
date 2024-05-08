package ru.semenov.calendar.dto.security;

public record RegisterRequest (
    String firstName,
    String lastName,
    String phoneNumber,
    String login,
    String password
) {
}
