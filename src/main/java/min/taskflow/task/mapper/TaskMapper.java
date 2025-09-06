package min.taskflow.task.mapper;

import min.taskflow.task.dto.request.TaskCreateRequest;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import min.taskflow.user.dto.response.UserSearchAndAssigneeResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskCreateRequest taskCreateRequest) {

        return Task.builder()
                .title(taskCreateRequest.title())
                .description(taskCreateRequest.description())
                .dueDate(taskCreateRequest.dueDate())
                .priority(taskCreateRequest.priority())
                .status(Status.TODO)
                .assigneeId(taskCreateRequest.assigneeId())
                .build();
    }

    public TaskResponse toTaskResponse(Task task, UserSearchAndAssigneeResponse assigneeResponse) {

        return TaskResponse.builder()
                .id(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(task.getStatus())
                .assigneeId(assigneeResponse.userid())
                .assigneeResponse(assigneeResponse)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
