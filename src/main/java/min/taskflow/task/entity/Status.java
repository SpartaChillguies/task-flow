package min.taskflow.task.entity;

import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;

public enum Status {
    TODO,
    IN_PROGRESS,
    DONE;

    public Status next() {
        return switch (this) {
            case TODO -> IN_PROGRESS;
            case IN_PROGRESS -> DONE;
            case DONE -> throw new TaskException(TaskErrorCode.INVALID_STATUS_UPDATE);
        };
    }
}
