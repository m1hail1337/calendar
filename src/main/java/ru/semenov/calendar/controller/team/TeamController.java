package ru.semenov.calendar.controller.team;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.semenov.calendar.dto.team.StatisticResponse;
import ru.semenov.calendar.dto.team.TeamCreationRequest;
import ru.semenov.calendar.service.team.TeamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Tag(name = "Контроллер управления командами",
     description = "Позволяет управлять командой и смотреть статистику")
@SecurityRequirement(name = "bearer")
@RestController("/calendar-api/teams")
public class TeamController {
    private final TeamService teamService;

    @Operation(summary = "Создает новую команду")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public String createTeam(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestBody TeamCreationRequest request) {
        checkManagerRole(authHeader);
        return teamService.createTeam(request);
    }

    @Operation(summary = "Добавляет сотрудника в команду")
    @PatchMapping("/{id}")
    public void addEmployeeToTeam(
        @PathVariable("id") String teamId,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestParam String employeeId) {
        checkManagerRole(authHeader);
        teamService.addEmployeeToTeam(teamId, employeeId);
    }

    @Operation(summary = "Выдает статистику по работе команды")
    @GetMapping("/{id}/statistic")
    public StatisticResponse getTeamStatistic(
        @PathVariable("id") String teamId,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        checkManagerRole(authHeader);
        return teamService.getTeamStatistic(teamId);
    }

    private void checkManagerRole(final String authHeader) {

    }
}
