package min.taskflow.task.dto.request;

import lombok.Builder;
import min.taskflow.task.entity.Status;

@Builder
public record StatusUpdateRequest(Status status) {
}
