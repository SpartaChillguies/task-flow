package min.taskflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
                "min.taskflow.task",
                "min.taskflow.user",
                // 공통 설정(@Configuration 등)을 쓰면 "min.taskflow.common"도 추가
        }
)
@EnableJpaRepositories(basePackages = {
        "min.taskflow.task",   // 필요한 레포만
        "min.taskflow.user"
})
@EntityScan(basePackages = {
        "min.taskflow.task.entity",
        "min.taskflow.user.entity",
        "min.taskflow.team.entity"   // ← team 엔티티만 스캔에 포함 (레포는 포함 안 해도 됨)
})
public class TaskFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskFlowApplication.class, args);
    }

}
