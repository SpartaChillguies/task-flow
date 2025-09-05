package min.taskflow.team.controller;

import lombok.RequiredArgsConstructor;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.team.dto.TeamCreateRequest;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.dto.TeamUpdateRequest;
import min.taskflow.team.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamController {

    private final TeamService teamService;

    // 팀 생성
    @PostMapping("/teams")
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(@RequestBody TeamCreateRequest request) {

        TeamResponse response = teamService.createTeam(request);
        return ApiResponse.created(response, "팀이 성공적으로 생성되었습니다.");
    }

    // 팀 단건 조회
    @GetMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeam(@PathVariable Long teamId) {

        TeamResponse response = teamService.getTeamById(teamId);
        return ApiResponse.success(response, "팀 단건 조회 성공했습니다.");
    }

    // 팀 전체 조회
    @GetMapping("/teams-names")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getAllTeams() {

        List<TeamResponse> responseList = teamService.getAllTeams();
        return ApiResponse.success(responseList, "팀 전체 조회 성공했습니다.");
    }

    // 팀 수정
    @PutMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(@PathVariable Long teamId,
                                                                @RequestBody TeamUpdateRequest request) {

        TeamResponse response = teamService.updateTeam(teamId, request);
        return ApiResponse.success(response, "팀 정보가 성공적으로 수정되었습니다.");
    }

    // 팀 삭제
    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(@PathVariable Long teamId) {

        teamService.deleteTeam(teamId);
        return ApiResponse.noContent("팀이 성공적으로 삭제되었습니다.");
    }
}
