package min.taskflow.task.service.queryService;

import lombok.RequiredArgsConstructor;
import min.taskflow.task.dto.condition.TaskSearchCondition;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;
import min.taskflow.task.mapper.TaskMapper;
import min.taskflow.task.repository.TaskRepository;
import min.taskflow.user.dto.response.UserSearchAndAssigneeResponse;
import min.taskflow.user.service.queryService.InternalQueryUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExternalQueryTaskService {

    private final TaskMapper taskMapper;
    private final InternalQueryUserService internalQueryUserService;
    private final TaskRepository taskRepository;

    //TODO: 리팩토링 필요 ( 동작 안됨 )
    public Page<TaskResponse> getTasksByTaskId(Pageable pageable, TaskSearchCondition condition) {

        // 2개 이상 또는 0개의 조건일 시 에러 발생
        isOnlyOne(condition);

        // 요청으로 들어온 1개의 조건으로 검색 수행 후 반환
        return searchLogic(pageable, condition);
    }

    public TaskResponse getTaskByTaskId(Long taskId) {

        // taskId에 해당하는 task가 존재하는지 검증
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        // User Data 불러오기
        UserSearchAndAssigneeResponse assigneeResponse = internalQueryUserService.getAssigneeByUserId(task.getAssigneeId());

        // DTO 변환
        TaskResponse taskResponse = taskMapper.toTaskResponse(task, assigneeResponse);

        return taskResponse;
    }

    // TODO: 헬퍼메서드 분리
    // 2개 이상 또는 0개의 조건일 시 에러 발생 ( 리팩토링 필요 )
    private void isOnlyOne(TaskSearchCondition condition) {

        int count = 0;
        if (condition.status() != null) {
            count++;
            Status status = condition.status();
        }
        if (condition.query() != null && !condition.query().isBlank()) {
            count++;
            String query = condition.query();
        }
        if (condition.assigneeId() != null) {
            count++;
            Long assigneeId = condition.assigneeId();
        }

        if (count == 0) {
            throw new TaskException(TaskErrorCode.AT_LEAST_ONE_REQUIRED);
        }

        if (count > 1) {
            throw new TaskException(TaskErrorCode.ONLY_ONE_ALLOWED);
        }
    }

    // TODO: 리팩토링 단계에서 로직 간략화하기
    public Page<TaskResponse> searchLogic(Pageable pageable, TaskSearchCondition condition) {
        if (condition.status() != null) {
            return taskRepository.findByStatus(condition.status(), pageable);
        }
        if (condition.query() != null) {
            return taskRepository.findByTitleContaining(condition.query(), pageable);
        }
        if (condition.assigneeId() != null) {
            return taskRepository.findByAssigneeId(condition.assigneeId(), pageable);
        }

        // 그 외 경우 빈 객체 반환
        return Page.empty(pageable);
    }
}
