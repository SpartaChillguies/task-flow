package min.taskflow.team.dto;

import java.time.LocalDateTime;

public record MemberResponse(
        Long id,
        String username,
        String name,
        String email,
        String role,
        LocalDateTime createdAt
) {}
