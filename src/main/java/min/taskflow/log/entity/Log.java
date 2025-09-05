package min.taskflow.log.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// TODO: 로그 생성일은 BaseEntity 상속 X, 혼자 만들어 쓰기
@Entity
public class Log {

    @Id
    private Long logId;

}
