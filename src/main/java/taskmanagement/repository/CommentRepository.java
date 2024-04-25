package taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanagement.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTaskIdOrderByIdDesc(Long taskId);
}
