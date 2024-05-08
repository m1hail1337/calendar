package ru.semenov.calendar.controller.team;

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
import ru.semenov.calendar.dto.task.TaskDto;
import ru.semenov.calendar.dto.task.creation.CreationRequest;
import ru.semenov.calendar.dto.task.edit.EditRequest;
import ru.semenov.calendar.service.task.TaskService;
import ru.semenov.calendar.utils.JwtTokenUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/calendar-api/team")
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер задач команды",
     description = "Описывает операции, которые можно выполнять с задачами, оперирует задачами команды")
@SecurityRequirement(name = "bearer")
public class TeamTaskController {
    private final JwtTokenUtils jwtTokenUtils;
    private final TaskService taskService;
    private final UserDbService userDbService;

    @Operation(summary = "Просмотр задач команды",
               description = """
                   Выводит все задачи команды страницами по 5 штук. Можно сортировать по дате создания задачи.
                   """)
    @GetMapping("{team_name}/tasks")
    public List<TaskDto> getAllTeamTasks(
        @PathVariable("team_name") String teamName,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestParam(name = "offset", defaultValue = "1") Integer offset,
        @RequestParam(name = "sortBy", defaultValue = "asc") String sortBy
    ) {
        Employee employee = getEmployeeFromAuthHeader(authHeader);
        return taskService.getEmployeeTasks(employee, offset, sortBy);
    }

    @Operation(summary = "Просмотр задачи",
               description = "Просмотр конкретной задачи в команде (по идентификатору)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @GetMapping("{team_name}/tasks/{id}")
    public TaskDto getTask(@PathVariable("team_name") String teamName, @PathVariable("id") long taskId) {
        return taskService.getTask(taskId);
    }

    @Operation(summary = "Создание задачи",
               description = """
                    Создает задачу с введенным пользователем сообщением,
                    ее статус при создании устанавливается как SKETCH (черновик)
                   """)
    @ApiResponse(responseCode = "201", description = "Заявка успешно создана")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("{team_name}/tasks/")
    public String createTask(
        @PathVariable("team_name") String teamName,
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
    @PatchMapping("{team_name}/tasks/{id}")
    public void editTask(
        @PathVariable("team_name") String teamName,
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
    @DeleteMapping("{team_name}/tasks/{id}")
    public void deleteTask(
        @PathVariable("team_name") String teamName,
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
