package min.taskflow.team.mapper;

import min.taskflow.team.dto.MemberResponse;
import min.taskflow.team.dto.TeamCreateRequest;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.dto.TeamUpdateRequest;
import min.taskflow.team.entity.Team;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class TeamMapper {

    public Team toEntity(TeamCreateRequest request) {
        return Team.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public void updateEntity(Team team, TeamUpdateRequest request) {
        team.updateTeam(request.name(), request.description());
    }

    public TeamResponse toTeamResponse(Team team) {
        return new TeamResponse(
                team.getTeamId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt(),
                team.getMembers() == null ? Collections.emptyList() :
                        team.getMembers().stream()
                                .map(user -> new MemberResponse(
                                        user.getUserId(),
                                        user.getUserName(),
                                        user.getName(),
                                        user.getEmail(),
                                        user.getRole().name(),
                                        user.getCreatedAt()
                                ))
                                .toList()
        );
    }
}

