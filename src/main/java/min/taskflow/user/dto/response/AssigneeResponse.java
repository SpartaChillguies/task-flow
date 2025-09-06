package min.taskflow.user.dto.response;

import lombok.Builder;

@Builder
public record AssigneeResponse(
        Long id,
        String userName,
        String name,
        String email
) {
}
