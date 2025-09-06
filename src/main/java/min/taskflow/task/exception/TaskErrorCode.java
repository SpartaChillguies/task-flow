package min.taskflow.task.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import min.taskflow.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TaskErrorCode implements ErrorCode {

    // 0XX 조회 에러
    TASK_NOT_FOUND("TASK-001", HttpStatus.NOT_FOUND, "태스크를 찾을 수 없습니다."),

    // 1XX 조회 조건 에러
    ONLY_ONE_ALLOWED("TASK-101", HttpStatus.UNPROCESSABLE_ENTITY, "오직 하나의 조건만 사용할 수 있습니다."),
    AT_LEAST_ONE_REQUIRED("TASK-102", HttpStatus.BAD_REQUEST, "검색을 위한 조건이 필요합니다."),

    // 2XX 수정 에러
    INVALID_STATUS_UPDATE("TASK-201", HttpStatus.BAD_REQUEST, "TASK 상태는 순차적 변경만 허용합니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
