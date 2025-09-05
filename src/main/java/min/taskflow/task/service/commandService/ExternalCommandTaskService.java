package min.taskflow.task.service.commandService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import min.taskflow.task.dto.request.StatusUpdateRequest;
import min.taskflow.task.dto.request.TaskCreateRequest;
import min.taskflow.task.dto.request.TaskUpdateRequest;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;
import min.taskflow.task.mapper.TaskMapper;
import min.taskflow.task.taskRepository.TaskRepository;
import min.taskflow.user.dto.response.AssigneeResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.repository.UserRepository;
import min.taskflow.user.service.InternalUserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExternalCommandTaskService {

    private final TaskMapper taskMapper;
    private final InternalUserService internalUserService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(TaskCreateRequest request) {

        Task task = taskMapper.toEntity(request);

        User user = userRepository.findById(request.assigneeId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        AssigneeResponse assigneeResponse = taskMapper.userToAssignee(user);

        return taskMapper.toTaskResponse(taskRepository.save(task), assigneeResponse);
    }

    public TaskResponse updateTaskDetailByTaskId(Long taskId, TaskUpdateRequest taskUpdateRequest) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        task.updateDetail(taskUpdateRequest);

        User user = userRepository.findById(taskUpdateRequest.assigneeId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        AssigneeResponse assigneeResponse = taskMapper.userToAssignee(user);

        TaskResponse taskResponse = taskMapper.toTaskResponse(task, assigneeResponse);

        return taskResponse;
    }

    public TaskResponse updateStatusByTaskId(Long taskId, StatusUpdateRequest statusUpdateRequest) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        Long assigneeId = task.getAssigneeId();

        User byId = userRepository.findById(assigneeId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        AssigneeResponse assigneeResponse = taskMapper.userToAssignee(byId);

        // 요청받은 status를 그 전 status와 비교해서 처리하는 로직
        Status status1 = task.getStatus();
        Status status2 = statusUpdateRequest.status();

        status1.validateTransition(status2);

        return taskMapper.updateStatus(task, status2, assigneeResponse);
    }

    //baseEntity로 softDelete
    public void deleteTaskByTaskId(Long taskId) {

        taskRepository.deleteById(taskId);
    }


}
