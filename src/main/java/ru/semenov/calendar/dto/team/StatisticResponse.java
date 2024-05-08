package ru.semenov.calendar.dto.team;

import java.util.*;

import ru.semenov.calendar.db.employee.Employee;

public record StatisticResponse(
    List<Employee> employees
) {
}
