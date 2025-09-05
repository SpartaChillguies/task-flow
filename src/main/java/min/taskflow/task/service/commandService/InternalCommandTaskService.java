package min.taskflow.task.service.commandService;

import lombok.RequiredArgsConstructor;
import min.taskflow.task.mapper.TaskMapper;
import min.taskflow.task.taskRepository.TaskRepository;
import min.taskflow.user.service.InternalUserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternalCommandTaskService {

    private final TaskMapper taskMapper;
    private final InternalUserService internalUserService;
    private final TaskRepository taskRepository;
}
