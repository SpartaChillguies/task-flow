package min.taskflow.task.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import min.taskflow.common.entity.BaseEntity;
import min.taskflow.task.dto.request.StatusUpdateRequest;
import min.taskflow.task.dto.request.TaskUpdateRequest;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long assigneeId;

    @Builder
    private Task(String title,
                 String description,
                 LocalDateTime dueDate,
                 Priority priority,
                 Status status,
                 Long assigneeId
    ) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.assigneeId = assigneeId;
    }

    public void updateDetail(TaskUpdateRequest taskUpdateRequest) {
        this.title = taskUpdateRequest.title();
        this.description = taskUpdateRequest.description();
        this.dueDate = taskUpdateRequest.dueDate();
        this.priority = taskUpdateRequest.priority();
        // TODO: status의 순차적 변경 로직 작성 후 아래 코드에 반영하기
        this.status = taskUpdateRequest.status();
        this.assigneeId = taskUpdateRequest.assigneeId();
    }

    public void updateStatus(StatusUpdateRequest statusUpdateRequest) {
        this.status = statusUpdateRequest.status();
    }
}
