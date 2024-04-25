package taskmanagement.repository;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taskmanagement.domain.Task;
import taskmanagement.domain.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskRepositoryTest {
    @Autowired UserRepository userRepository;
    @Autowired TaskRepository taskRepository;

    @Test
    void 성공_테스트_생성() {
        // given
        Task task = new Task();
        task.setTitle("hello");
        task.setDescription("world");

        // when
        Task save = this.taskRepository.save(task);
        Task find = this.taskRepository.findById(save.getId()).get();

        // then
        Assertions.assertThat(find).isEqualTo(save);
    }
}