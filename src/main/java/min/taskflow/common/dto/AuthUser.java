package min.taskflow.common.dto;

import lombok.Getter;
import min.taskflow.user.enums.UserRole;

/**
 * 인증된 사용자 정보를 담는 DTO.
 * - 컨트롤러 메서드 파라미터에서 주입받아 사용
 * - JWT 검증 후 추출한 userId, userRole 을 보관
 */
@Getter
public class AuthUser {

    private final Long id;
    private final UserRole userRole;

    public AuthUser(Long id, UserRole userRole) {
        this.id = id;
        this.userRole = userRole;
    }
}
