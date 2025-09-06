package min.taskflow.task.controllder;

import lombok.RequiredArgsConstructor;
import min.taskflow.common.response.ApiPageResponse;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.task.dto.condition.TaskSearchCondition;
import min.taskflow.task.dto.request.StatusUpdateRequest;
import min.taskflow.task.dto.request.TaskCreateRequest;
import min.taskflow.task.dto.request.TaskUpdateRequest;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.service.commandService.ExternalCommandTaskService;
import min.taskflow.task.service.queryService.ExternalQueryTaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final ExternalCommandTaskService externalCommandTaskService;
    private final ExternalQueryTaskService externalQueryTaskService;

    // 태스크 생성
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@RequestBody TaskCreateRequest request) {

        TaskResponse taskResponse = externalCommandTaskService.createTask(request);

        return ApiResponse.created(taskResponse, "Task를 생성하였습니다.");
    }

    // 검색 조건에 따라 태스크 조회
    @GetMapping
    public ResponseEntity<ApiPageResponse<TaskResponse>> getTasksByStatusOrQueryOrAssigneeId(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                             @RequestParam(required = false, defaultValue = "10") int size,
                                                                                             @ModelAttribute TaskSearchCondition condition) {

        Pageable pageable = PageRequest.of(page, size);

        Page<TaskResponse> tasks = externalQueryTaskService.getTasksByTaskId(pageable, condition);

        return ApiPageResponse.success(tasks, "Task 목록을 조회했습니다.");
    }

    // 태스크 상세 조회
    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskByTaskId(@PathVariable Long taskId) {

        TaskResponse task = externalQueryTaskService.getTaskByTaskId(taskId);

        return ApiResponse.success(task, "Task를 조회했습니다.");
    }

    // 태스크 상세 수정
    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskDetailByTaskId(@PathVariable Long taskId,
                                                                              @RequestBody TaskUpdateRequest taskUpdateRequest) {

        TaskResponse taskResponse = externalCommandTaskService.updateTaskDetailByTaskId(taskId, taskUpdateRequest);

        return ApiResponse.success(taskResponse, "Task가 수정되었습니다.");
    }

    // 태스크 상태 수정
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateStatusByTaskId(@PathVariable Long taskId,
                                                                          @RequestBody StatusUpdateRequest statusUpdateRequest) {

        TaskResponse taskResponse = externalCommandTaskService.updateStatusByTaskId(taskId, statusUpdateRequest);

        return ApiResponse.success(taskResponse, "작업 상태가 업데이트되었습니다.");
    }

    // 태스크 삭제(soft delete)
    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Object>> deleteTaskByTaskId(@PathVariable Long taskId) {

        externalCommandTaskService.deleteTaskByTaskId(taskId);

        return ApiResponse.noContent("Task가 삭제되었습니다.");
    }
}
