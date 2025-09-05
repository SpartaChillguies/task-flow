package min.taskflow.task.exception;

import min.taskflow.common.exception.ErrorCode;
import min.taskflow.common.exception.GlobalException;


public class TaskException extends GlobalException {
    public TaskException(ErrorCode errorCode) {
        super(errorCode);
    }
}
