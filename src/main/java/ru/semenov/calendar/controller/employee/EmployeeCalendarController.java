package ru.semenov.calendar.controller.employee;

import java.util.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.semenov.calendar.db.employee.Employee;
import ru.semenov.calendar.db.security.user.UserDbService;
import ru.semenov.calendar.dto.calendar.DayEvent;
import ru.semenov.calendar.dto.task.TaskDto;
import ru.semenov.calendar.dto.task.creation.CreationRequest;
import ru.semenov.calendar.dto.task.edit.EditRequest;
import ru.semenov.calendar.service.calendar.CalendarService;
import ru.semenov.calendar.utils.JwtTokenUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/calendar-api/employee/calendar")
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер задач сотрудника",
     description = "Описывает операции, которые можно выполнять с задачами, все задачи принадлежат сотруднику")
@SecurityRequirement(name = "bearer")
public class EmployeeCalendarController {
    private final JwtTokenUtils jwtTokenUtils;
    private final CalendarService calendarService;
    private final UserDbService userDbService;

    @Operation(summary = "Просмотр календаря пользователя",
               description = """
                   Выводит все задачи пользователя страницами по 5 штук. Можно сортировать по дате создания задачи.
                   """)
    @GetMapping
    public List<DayEvent> getAllEmployeeCalendar(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestParam(name = "offset", defaultValue = "1") Integer offset,
        @RequestParam(name = "sortBy", defaultValue = "asc") String sortBy
    ) {
        Employee employee = getEmployeeFromAuthHeader(authHeader);
        return calendarService.getEmployeeCalendar(employee, offset, sortBy);
    }

    @Operation(summary = "Просмотр задач и событий дня в календаре",
               description = "Просмотр конкретной задачи (по идентификатору), созданной пользователем")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Задачи или события на этот день не найдены")
    })
    @GetMapping("/{day}")
    public TaskDto getTask(@PathVariable("day") int day) {
        return calendarService.getDayEvents(day);
    }

    @Operation(summary = "Создание события",
               description = """
                    Создает событие с введенным пользователем сообщением,
                    ее статус при создании устанавливается как SKETCH (черновик)
                   """)
    @ApiResponse(responseCode = "201", description = "Задача успешно создана")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events")
    public String createTask(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestBody CreationRequest creationRequest) {
        Employee employee = getEmployeeFromAuthHeader(authHeader);
        return calendarService.createEvent(creationRequest, employee);
    }

    @Operation(summary = "Редактирование события", description = "Изменяет информацию в задаче")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Задача успешно отредактирована"),
        @ApiResponse(responseCode = "403", description = "Нет прав для изменения задачи")
    })
    @PatchMapping("/events/{day}")
    public void editTask(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestBody EditRequest request,
        @PathVariable("day") long day) {
        Employee employee = getEmployeeFromAuthHeader(authHeader);
        calendarService.editEvent(request, employee, day);
    }

    @Operation(summary = "Удаление события")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Событие успешно удалено"),
        @ApiResponse(responseCode = "403", description = "Нет прав для удаления события")
    })
    @DeleteMapping("/events/{id}")
    public void deleteEvent(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader,
        @PathVariable("id") long eventId) {
        Employee employee = getEmployeeFromAuthHeader(authHeader);
        calendarService.deleteEvent(employee, eventId);
    }

    private Employee getEmployeeFromAuthHeader(String authHeader) {
        log.info(authHeader);
        String jwt = jwtTokenUtils.parseBearerJwtFromHeader(authHeader);
        log.info(jwt);
        String login = jwtTokenUtils.getUsername(jwt);
        log.info(login);
        return userDbService.findByLogin(login).orElseThrow(
            () -> new NoSuchElementException("Пользователь не найден или неверный токен")
        ).getEmployee();
    }
}
