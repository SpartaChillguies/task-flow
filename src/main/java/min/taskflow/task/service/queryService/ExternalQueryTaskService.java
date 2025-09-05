package min.taskflow.task.service.queryService;

import lombok.RequiredArgsConstructor;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;
import min.taskflow.task.mapper.TaskMapper;
import min.taskflow.task.taskRepository.TaskRepository;
import min.taskflow.user.dto.response.AssigneeResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.service.InternalUserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExternalQueryTaskService {

    private final TaskMapper taskMapper;
    private final InternalUserService internalUserService;
    private final TaskRepository taskRepository;

    public TaskResponse getTaskByTaskId(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        Long assigneeId = task.getAssigneeId();
        User byUserId = internalUserService.findByUserId(assigneeId);
        AssigneeResponse assigneeResponse = taskMapper.userToAssignee(byUserId);

        return taskMapper.toTaskResponse(task,assigneeResponse);
    }

    public Page<TaskResponse> getTasks(String keyword, Status status, Long assigneeId, Pageable pageable) {
        Page<Task> page = taskRepository.search(keyword, status, assigneeId, pageable);

        return page.map(TaskResponse::from);
    }
}
