package min.taskflow.task.controllder;

import lombok.RequiredArgsConstructor;
import min.taskflow.common.annotation.Auth;
import min.taskflow.common.dto.AuthUser;
import min.taskflow.common.response.ApiPageResponse;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.task.dto.request.StatusUpdateRequest;
import min.taskflow.task.dto.request.TaskCreateRequest;
import min.taskflow.task.dto.request.TaskUpdateRequest;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.entity.Status;
import min.taskflow.task.service.commandService.ExternalCommandTaskService;
import min.taskflow.task.service.queryService.ExternalQueryTaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final ExternalCommandTaskService externalCommandTaskService;
    private final ExternalQueryTaskService externalQueryTaskService;

    //authUser로 로그인되어있는 유저의 아이디를  뽑아서 사용
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@RequestBody TaskCreateRequest request) {

        TaskResponse taskResponse = externalCommandTaskService.createTask(request);

        return ApiResponse.created(taskResponse, "Task를 생성하였습니다.");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ApiPageResponse<TaskResponse>>> getTasksByStatusOrQueryOrAssigneeId(
            @RequestParam(required = false) String keyWord,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Long assigneeId,
            Pageable pageable) {

        Page<TaskResponse> tasks = externalQueryTaskService.getTasks(keyWord, status, assigneeId, pageable);

        // ApiPageResponse가 Page<T>를 받도록 되어 있다면 그대로
        return ApiPageResponse.success(tasks, "Task 목록을 조회했습니다.", HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskByTaskId(@PathVariable Long taskId) {

        TaskResponse task = externalQueryTaskService.getTaskByTaskId(taskId);

        return ApiResponse.success(task, "Task를 조회했습니다.");
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskDetailByTaskId(@PathVariable Long taskId,
                                                                              TaskUpdateRequest taskUpdateRequest
    ) {

        TaskResponse taskResponse = externalCommandTaskService.updateTaskDetailByTaskId(taskId, taskUpdateRequest);

        return ApiResponse.success(taskResponse, "Task가 수정되었습니다.");
    }

    //Status 순차적 변경 적용
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateStatusByTaskId(@PathVariable Long taskId,
                                                                          StatusUpdateRequest statusUpdateRequest) {

        TaskResponse taskResponse = externalCommandTaskService.updateStatusByTaskId(taskId, statusUpdateRequest);

        return ApiResponse.success(taskResponse, "작업 상태가 업데이트되었습니다.");
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Object>> deleteTaskByTaskId(@PathVariable Long taskId) {

        externalCommandTaskService.deleteTaskByTaskId(taskId);

        return ApiResponse.noContent("Task가 삭제되었습니다.");
    }
}
