package min.taskflow.task.mapper;

import min.taskflow.task.dto.request.TaskCreateRequest;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import min.taskflow.user.dto.response.AssigneeResponse;
import min.taskflow.user.entity.User;
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

    public TaskResponse toTaskResponse(Task task, AssigneeResponse assigneeResponse) {

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(task.getStatus())
                .assigneeId(assigneeResponse.id())
                .assigneeResponse(assigneeResponse)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    //user를 관리자로 변환
    public AssigneeResponse userToAssignee(User user){

        return AssigneeResponse.builder()
                .id(user.getUserId())
                .userName(user.getUserName())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public TaskResponse updateStatus(Task task, Status status,AssigneeResponse assigneeResponse) {

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(status)
                .assigneeId(task.getAssigneeId())
                .assigneeResponse(assigneeResponse)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
