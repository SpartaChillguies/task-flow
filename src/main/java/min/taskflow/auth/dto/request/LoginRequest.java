package min.taskflow.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

/*
로그인시 요청dto
 */
public record LoginRequest(
        @NotBlank(message = "유저네임은 공백일 수 없습니다.") String username,
        @NotBlank(message = "비밀번호는 공백일 수 없습니다.") String password) {
}
