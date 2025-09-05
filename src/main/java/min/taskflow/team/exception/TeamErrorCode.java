package min.taskflow.team.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import min.taskflow.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamErrorCode implements ErrorCode {

    TEAM_NOT_FOUND("TEAM-001",HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다."),
    DUPLICATE_TEAM_NAME("TEAM-002", HttpStatus.CONFLICT, "이미 존재하는 팀 이름입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
