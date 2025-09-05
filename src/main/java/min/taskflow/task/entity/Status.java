package min.taskflow.task.entity;

// TODO: 순차적 변경에 대한 논의 후 로직 작성하기.
public enum Status {
    TODO,
    IN_PROGRESS,
    DONE;

    //다음 상태를 명시합니다
    public Status next() {
        return switch (this) {
            case TODO -> IN_PROGRESS;
            case IN_PROGRESS -> DONE;
            case DONE -> TODO;
        };
    }

    // 상태 변환시 유효성을 확인합니다.
    public void validateTransition(Status target){
        if (target.ordinal() < this.ordinal()){
            throw new IllegalStateException(
                    String.format("현재 상태 %s 에서 이전 상태 %s 로 되돌릴 수 없습니다.", this, target)
            );
        }
        if (target.ordinal() - this.ordinal() > 1) {
            throw new IllegalStateException(
                    String.format("상태는 순차적으로만 진행 가능합니다. 현재 %s 에서 %s 로 건너뛸 수 없습니다.", this, target)
            );
        }
    }
}
