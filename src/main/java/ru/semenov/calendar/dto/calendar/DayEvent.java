package ru.semenov.calendar.dto.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.semenov.calendar.db.task.Task;

public record DayEvent(
    @JsonProperty(value = "date_time", required = true)
    String dateTime,
    //EventType type,
    Task task
    //Event event
) {
}
