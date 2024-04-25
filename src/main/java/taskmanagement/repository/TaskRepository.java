package taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanagement.domain.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByOrderByIdDesc();

    List<Task> findAllByAuthorEmailIgnoreCaseAndAssigneeEmailIgnoreCaseOrderByIdDesc(String authorEmail, String assigneeEmail);

    List<Task> findAllByAuthorEmailIgnoreCaseOrderByIdDesc(String authorEmail);

    List<Task> findAllByAssigneeEmailIgnoreCaseOrderByIdDesc(String assigneeEmail);
}
