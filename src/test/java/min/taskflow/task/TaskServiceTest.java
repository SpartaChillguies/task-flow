package min.taskflow.task;

import min.taskflow.task.dto.request.TaskCreateRequest;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.entity.Priority;
import min.taskflow.task.entity.Task;
import min.taskflow.task.mapper.TaskMapper;
import min.taskflow.task.service.commandService.ExternalCommandTaskService;
import min.taskflow.user.dto.response.UserSearchAndAssigneeResponse;
import min.taskflow.user.service.queryService.InternalQueryUserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private ExternalCommandTaskService externalCommandTaskService;

    @Mock
    private InternalQueryUserService internalQueryUserService;
    @Mock
    private TaskMapper taskMapper;

    void createTask_태스크_생성_성공() {

        // given
        Long userId = 1L;
        Long assigneeId = 2L;
        TaskCreateRequest request = new TaskCreateRequest("프로젝트 기획안 수정",
                "2024년 1분기 프로젝트 기획안 수정하기",
                LocalDateTime.parse("2024-04-02T23:59:59Z"),
                Priority.MEDIUM,
                assigneeId);

        Task task = taskMapper.toEntity(request);

        UserSearchAndAssigneeResponse assigneeResponse = internalQueryUserService
                .getAssigneeByUserId(assigneeId);

        TaskResponse response = taskMapper.toTaskResponse(task, assigneeResponse);
    }

}
