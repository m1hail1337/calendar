package ru.semenov.calendar.service.calendar;

import java.util.*;

import org.springframework.stereotype.Service;
import ru.semenov.calendar.db.employee.Employee;
import ru.semenov.calendar.dto.calendar.DayEvent;
import ru.semenov.calendar.dto.task.TaskDto;
import ru.semenov.calendar.dto.task.creation.CreationRequest;
import ru.semenov.calendar.dto.task.edit.EditRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarService {
    //TODO
    public List<DayEvent> getEmployeeCalendar(final Employee employee, final Integer offset, final String sortBy) {
        return null;
    }
    //TODO
    public TaskDto getDayEvents(final int day) {
        return null;
    }
    //TODO
    public String createEvent(final CreationRequest creationRequest, final Employee employee) {

        return null;
    }
    //TODO
    public void editEvent(final EditRequest request, final Employee employee, final long day) {

    }
    //TODO
    public List<DayEvent> getTeamCalendar(final Employee employee, final Integer offset, final String sortBy) {
        return null;
    }
    //TODO
    public void deleteEvent(final Employee employee, final long eventId) {

    }
}
