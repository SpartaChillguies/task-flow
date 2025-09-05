package min.taskflow.task.taskRepository;

import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import java.awt.print.Pageable;
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

    @Query("""
        select t from Task t
        where (:keyword is null or :keyword = '' 
               or lower(t.title) like lower(concat('%', :keyword, '%')) 
               or lower(t.description) like lower(concat('%', :keyword, '%')))
          and (:status is null or t.status = :status)
          and (:assigneeId is null or t.assignee.id = :assigneeId)
    """)
    Page<Task> search(@Param("keyword") String keyword,
                      @Param("status") Status status,
                      @Param("assigneeId") Long assigneeId,
                      Pageable pageable);

}
