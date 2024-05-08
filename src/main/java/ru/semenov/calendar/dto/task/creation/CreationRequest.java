package ru.semenov.calendar.dto.task.creation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record CreationRequest (
        String message,
        @JsonProperty("creation_time")
        String creationTime
) { }
