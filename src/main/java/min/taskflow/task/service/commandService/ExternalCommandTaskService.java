package min.taskflow.task.service.commandService;

import lombok.RequiredArgsConstructor;
import min.taskflow.task.dto.request.StatusUpdateRequest;
import min.taskflow.task.dto.request.TaskCreateRequest;
import min.taskflow.task.dto.request.TaskUpdateRequest;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.entity.Task;
import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;
import min.taskflow.task.mapper.TaskMapper;
import min.taskflow.task.repository.TaskRepository;
import min.taskflow.user.dto.response.UserSearchAndAssigneeResponse;
import min.taskflow.user.service.queryService.InternalQueryUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExternalCommandTaskService {

    private final TaskMapper taskMapper;
    private final InternalQueryUserService internalQueryUserService;
    private final TaskRepository taskRepository;

    public TaskResponse createTask(TaskCreateRequest request) {

        Task task = taskMapper.toEntity(request);

        // User Data 불러오기
        UserSearchAndAssigneeResponse assigneeResponse = internalQueryUserService.getAssigneeByUserId(request.assigneeId());

        // Task 저장 및 DTO 변환
        TaskResponse taskResponse = taskMapper.toTaskResponse(taskRepository.save(task), assigneeResponse);

        return taskResponse;
    }

    public TaskResponse updateTaskDetailByTaskId(Long taskId, TaskUpdateRequest taskUpdateRequest) {

        // taskId에 해당하는 task가 존재하는지 검증
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        // Task 상세 업데이트
        task.updateDetail(taskUpdateRequest);

        // User Data 불러오기
        UserSearchAndAssigneeResponse assigneeResponse = internalQueryUserService.getAssigneeByUserId(task.getAssigneeId());

        // DTO 변환
        TaskResponse taskResponse = taskMapper.toTaskResponse(task, assigneeResponse);

        return taskResponse;
    }

    public TaskResponse updateStatusByTaskId(Long taskId, StatusUpdateRequest statusUpdateRequest) {

        // taskId에 해당하는 task가 존재하는지 검증
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        // TODO: 순차적 변경이 아닌 경우에 대한 에러 추가
        task.updateStatus(statusUpdateRequest);

        // User Data 불러오기
        UserSearchAndAssigneeResponse assigneeResponse = internalQueryUserService.getAssigneeByUserId(task.getAssigneeId());

        // DTO 변환
        TaskResponse taskResponse = taskMapper.toTaskResponse(task, assigneeResponse);

        return taskResponse;
    }

    public void deleteTaskByTaskId(Long taskId) {

        // Task soft delete
        taskRepository.deleteById(taskId);
    }

}
