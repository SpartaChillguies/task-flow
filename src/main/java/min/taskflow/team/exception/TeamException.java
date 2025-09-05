package min.taskflow.team.exception;

import min.taskflow.common.exception.ErrorCode;
import min.taskflow.common.exception.GlobalException;

public class TeamException extends GlobalException {

    public TeamException(ErrorCode errorCode){
        super(errorCode);
    }
}
