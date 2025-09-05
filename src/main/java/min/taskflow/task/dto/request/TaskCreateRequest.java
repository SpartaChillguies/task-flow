package min.taskflow.task.dto.request;

import lombok.Builder;
import lombok.Getter;
import min.taskflow.task.entity.Priority;

import java.time.LocalDateTime;

@Builder
public record TaskCreateRequest(
        String title,
        String description,
        LocalDateTime dueDate,
        Priority priority,
        Long assigneeId
) {
}
