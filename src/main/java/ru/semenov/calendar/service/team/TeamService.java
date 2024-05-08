package ru.semenov.calendar.service.team;

import org.springframework.stereotype.Service;
import ru.semenov.calendar.dto.team.StatisticResponse;
import ru.semenov.calendar.dto.team.TeamCreationRequest;

@Service
public class TeamService {
    public String createTeam(final TeamCreationRequest request) {
        return null;
    }

    public void addEmployeeToTeam(final String teamId, final String employeeId) {

    }

    public StatisticResponse getTeamStatistic(final String teamId) {
        return null;
    }
}
