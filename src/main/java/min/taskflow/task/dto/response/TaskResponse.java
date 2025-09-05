package min.taskflow.task.dto.response;

import lombok.Builder;
import min.taskflow.task.entity.Priority;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import min.taskflow.user.dto.response.AssigneeResponse;
import min.taskflow.user.repository.UserRepository;

import java.time.LocalDateTime;

@Builder
public record TaskResponse(Long id,
                           String title,
                           String description,
                           LocalDateTime dueDate,
                           Priority priority,
                           Status status,
                           Long assigneeId,
                           AssigneeResponse assigneeResponse,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt) {

    public static TaskResponse from(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(task.getStatus())
                .assigneeId(task.getAssigneeId())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}