package taskmanagement.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taskmanagement.web.dto.TaskDto;

@Transactional
@SpringBootTest
class TaskServiceTest {
    @Autowired
    TaskService taskService;
    @Autowired
    UserService userService;

    @Test
    void 성공_태스크_생성() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("title");
        taskDto.setDescription("description");
//        this.taskService.createTask(taskDto);
    }

    @Test
    void 성공_태스트_할당() {

    }

    @Test
    void 오류_태스크_할당은생성자만가능() {

    }

    @Test
    void 성공_태스크_상태변경() {

    }

    @Test
    void 실패_태스크_상태변경은관계자만가능() {

    }

    @Test
    void 성공_코멘트_작성() {

    }
}