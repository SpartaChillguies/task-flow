package min.taskflow.user.dto.response;

import lombok.Builder;
import min.taskflow.team.entity.Team;
import min.taskflow.user.enums.UserRole;

@Builder
public record UserResponse(Long userId,
                           String userName,
                           String password,
                           String email,
                           String name,
                           UserRole role,
                           Team team) {
}
