package min.taskflow.task.dto.condition;

import min.taskflow.task.entity.Status;

public record TaskSearchCondition(
        Status status,
        String query,
        Long assigneeId
) {
}