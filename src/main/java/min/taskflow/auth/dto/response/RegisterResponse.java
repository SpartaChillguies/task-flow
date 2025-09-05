package min.taskflow.auth.dto.response;

import lombok.Builder;
import min.taskflow.user.enums.UserRole;

import java.time.LocalDateTime;

/*
회원가입시 응답 DTO
 */
@Builder
public record RegisterResponse(
        Long id,
        String username,
        String email,
        String name,
        UserRole role,
        LocalDateTime createdAt) {

}
