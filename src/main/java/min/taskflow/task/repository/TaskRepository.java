package min.taskflow.task.repository;

import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task save(Task task);

    //쿼리문으로 검색 수행
    @Query("SELECT t FROM Task t " +
            "WHERE t.title LIKE %:keyword% " +
            "OR t.description LIKE %:keyword%")
    List<Task> findByKeyword(String keyWord);

    Optional<Task> findById(Long taskId);

    Page<TaskResponse> findByStatus(Status status, Pageable pageable);

    Page<TaskResponse> findByTitleContaining(String query, Pageable pageable);

    Page<TaskResponse> findByAssigneeId(Long aLong, Pageable pageable);
}
