package min.taskflow.team.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TeamResponse(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        List<MemberResponse> members
) {}
