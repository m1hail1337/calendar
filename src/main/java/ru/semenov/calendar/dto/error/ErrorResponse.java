package ru.semenov.calendar.dto.error;

import lombok.Builder;

@Builder
public record ErrorResponse(
    String message
) {
}
