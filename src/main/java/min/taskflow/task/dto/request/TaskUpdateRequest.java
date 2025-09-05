package min.taskflow.task.dto.request;

import lombok.Builder;
import min.taskflow.task.entity.Priority;
import min.taskflow.task.entity.Status;

import java.time.LocalDateTime;

@Builder
public record TaskUpdateRequest(
        String title,
        String description,
        LocalDateTime dueDate,
        Priority priority,
        Status status,
        Long assigneeId) {
}
