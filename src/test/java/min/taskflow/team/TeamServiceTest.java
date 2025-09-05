package min.taskflow.team;

import min.taskflow.team.dto.TeamCreateRequest;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.dto.TeamUpdateRequest;
import min.taskflow.team.entity.Team;
import min.taskflow.team.exception.TeamException;
import min.taskflow.team.mapper.TeamMapper;
import min.taskflow.team.repository.TeamRepository;
import min.taskflow.team.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static min.taskflow.team.exception.TeamErrorCode.TEAM_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TeamServiceTest {

    private TeamRepository teamRepository;
    private TeamMapper teamMapper;
    private TeamService teamService;

    @BeforeEach
    void setUp() {

        teamRepository = mock(TeamRepository.class); // 레포지토리 모킹
        teamMapper = new TeamMapper();
        teamService = new TeamService(teamRepository, teamMapper);
    }

    @Test
    void 팀을_성공적으로_생성했습니다() {

        TeamCreateRequest request = new TeamCreateRequest("개발팀", "백엔드/프론트");
        Team savedTeam = Team.builder()
                .name(request.name())
                .description(request.description())
                .build();

        // Reflection으로 teamId 세팅
        ReflectionTestUtils.setField(savedTeam, "teamId", 1L);

        when(teamRepository.existsByName(request.name())).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenReturn(savedTeam);

        TeamResponse response = teamService.createTeam(request);

        assertEquals("개발팀", response.name());
        assertEquals("백엔드/프론트", response.description());
    }

    @Test
    void 존재하지_않는_팀_조회() {

        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        TeamException exception = assertThrows(TeamException.class,
                () -> teamService.getTeamById(1L));

        assertEquals(TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 팀을_성공적으로_조회합니다() {

        Team team = Team.builder()
                .name("개발팀")
                .description("백엔드/프론트")
                .build();
        ReflectionTestUtils.setField(team, "teamId", 1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        TeamResponse response = teamService.getTeamById(1L);

        assertEquals("개발팀", response.name());
        assertEquals("백엔드/프론트", response.description());
    }

    @Test
    void 팀을_성공적으로_수정합니다() {

        Team team = Team.builder()
                .name("개발팀")
                .description("백엔드/프론트")
                .build();
        ReflectionTestUtils.setField(team, "teamId", 1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.existsByName("디자인팀")).thenReturn(false);

        TeamUpdateRequest request = new TeamUpdateRequest("디자인팀", "UI/UX 담당");
        TeamResponse response = teamService.updateTeam(1L, request);

        assertEquals("디자인팀", response.name());
        assertEquals("UI/UX 담당", response.description());
    }

    @Test
    void 존재하지_않는_팀은_수정할_수_없습니다() {

        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        TeamUpdateRequest request = new TeamUpdateRequest("디자인팀", "UI/UX 담당");

        TeamException exception = assertThrows(TeamException.class,
                () -> teamService.updateTeam(999L, request));

        assertEquals(TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 팀을_성공적으로_삭제합니다() {

        Team team = Team.builder()
                .name("개발팀")
                .description("백엔드/프론트")
                .build();
        ReflectionTestUtils.setField(team, "teamId", 1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        teamService.deleteTeam(1L);

        assertTrue(team.isDeleted());
        assertNotNull(team.getDeletedAt());
        verify(teamRepository, never()).save(any());
    }

    @Test
    void 존재하지_않는_팀은_삭제할_수_없습니다() {

        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        TeamException exception = assertThrows(TeamException.class,
                () -> teamService.deleteTeam(999L));

        assertEquals(TEAM_NOT_FOUND, exception.getErrorCode());
    }
}
