package taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import taskmanagement.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

//    @Query("SELECT t FROM Transaction t JOIN FETCH t.card WHERE t.transactionId = ?1")

    @Query("SELECT u from User u join fetch u.tasks t where u.email= ?1 order by t.id desc")
    Optional<User> findByEmailIgnoreCaseWithTasksOrderByIdDesc(String email);
}
