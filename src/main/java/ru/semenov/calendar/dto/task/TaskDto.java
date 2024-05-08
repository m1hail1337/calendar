package ru.semenov.calendar.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import ru.semenov.calendar.db.task.TaskStatus;

import java.time.LocalDateTime;

@Builder
public record TaskDto(
        long id,
        TaskStatus status,
        String message,
        @JsonProperty("creation_date")
        LocalDateTime creationDate,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("phone_number")
        String phoneNumber
) {
}
