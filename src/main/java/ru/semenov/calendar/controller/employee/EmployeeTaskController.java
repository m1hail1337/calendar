package ru.semenov.calendar.controller.employee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.semenov.calendar.db.employee.Employee;
import ru.semenov.calendar.db.security.user.UserDbService;
import ru.semenov.calendar.dto.task.TaskDto;
import ru.semenov.calendar.dto.task.creation.CreationRequest;
import ru.semenov.calendar.dto.task.edit.EditRequest;
import ru.semenov.calendar.service.task.TaskService;
import ru.semenov.calendar.utils.JwtTokenUtils;

import java.util.*;

@RequestMapping("/calendar-api/employee/tasks")
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер задач сотрудника",
     description = "Описывает операции, которые можно выполнять с задачами, все задачи принадлежат сотруднику")
@SecurityRequirement(name = "bearer")
public class EmployeeTaskController {
    private final JwtTokenUtils jwtTokenUtils;
    private final TaskService taskService;
    private final UserDbService userDbService;

    @Operation(summary = "Просмотр задач, созданных пользователем",
               description = """
                   Выводит все задачи пользователя страницами по 5 штук. Можно сортировать по дате создания задачи.
                   """)
    @GetMapping
    public List<TaskDto> getAllEmployeeTasks(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestParam(name = "offset", defaultValue = "1") Integer offset,
        @RequestParam(name = "sortBy", defaultValue = "asc") String sortBy
    ) {
        Employee employee = getEmployeeFromAuthHeader(authHeader);
        return taskService.getEmployeeTasks(employee, offset, sortBy);
    }

    @Operation(summary = "Просмотр задачи",
               description = "Просмотр конкретной задачи (по идентификатору), созданной пользователем")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @GetMapping("{id}")
    public TaskDto getTask(@PathVariable("id") long taskId) {
        return taskService.getTask(taskId);
    }

    @Operation(summary = "Создание задачи",
               description = """
                    Создает задачу с введенным пользователем сообщением,
                    ее статус при создании устанавливается как SKETCH (черновик)
                   """)
    @ApiResponse(responseCode = "201", description = "Задача успешно создана")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public String createTask(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestBody CreationRequest creationRequest) {
        Employee employee = getEmployeeFromAuthHeader(authHeader);
        return taskService.createTask(creationRequest, employee);
    }

    @Operation(summary = "Редактирование задачи", description = "Изменяет информацию в задаче")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Задача успешно отредактирована"),
        @ApiResponse(responseCode = "403", description = "Нет прав для изменения задачи")
    })
    @PatchMapping("{id}")
    public void editTask(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestBody EditRequest request,
        @PathVariable("id") long taskId) {
        Employee employee = getEmployeeFromAuthHeader(authHeader);
        taskService.editTask(request, employee, taskId);
    }

    @Operation(summary = "Удаление задачи")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Задача успешно удалена"),
        @ApiResponse(responseCode = "403", description = "Нет прав для удаления задачи")
    })
    @DeleteMapping("{id}")
    public void deleteTask(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader,
        @PathVariable("id") long taskId) {
        Employee employee = getEmployeeFromAuthHeader(authHeader);
        taskService.deleteTask(employee, taskId);
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
