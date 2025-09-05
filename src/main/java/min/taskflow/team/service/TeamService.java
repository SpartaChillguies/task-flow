package min.taskflow.team.service;

import lombok.RequiredArgsConstructor;
import min.taskflow.team.dto.TeamCreateRequest;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.dto.TeamUpdateRequest;
import min.taskflow.team.entity.Team;
import min.taskflow.team.exception.TeamException;
import min.taskflow.team.mapper.TeamMapper;
import min.taskflow.team.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static min.taskflow.team.exception.TeamErrorCode.DUPLICATE_TEAM_NAME;
import static min.taskflow.team.exception.TeamErrorCode.TEAM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    // 팀 생성
    @Transactional
    public TeamResponse createTeam(TeamCreateRequest request) {

        if (teamRepository.existsByName(request.name())) {
            throw new TeamException(DUPLICATE_TEAM_NAME);
        }

        Team team = teamMapper.toEntity(request);
        teamRepository.save(team);

        return teamMapper.toTeamResponse(team);
    }

    // 팀 단건 조회
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(TEAM_NOT_FOUND));

        return teamMapper.toTeamResponse(team);
    }

    // 팀 전체 조회
    @Transactional(readOnly = true)
    public List<TeamResponse> getAllTeams() {

        return teamRepository.findAll().stream()
                .map(teamMapper::toTeamResponse)
                .toList();
    }

    // 팀 수정
    @Transactional
    public TeamResponse updateTeam(Long teamId, TeamUpdateRequest request) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(TEAM_NOT_FOUND));

        if (!team.getName().equals(request.name()) && teamRepository.existsByName(request.name())) {
            throw new TeamException(DUPLICATE_TEAM_NAME);
        }

        teamMapper.updateEntity(team, request);
        return teamMapper.toTeamResponse(team);
    }

    // 팀 삭제
    @Transactional
    public void deleteTeam(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(TEAM_NOT_FOUND));

        team.delete();
    }
}
