package min.taskflow.user.exception;

import min.taskflow.common.exception.ErrorCode;
import min.taskflow.common.exception.GlobalException;


public class UserException extends GlobalException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
