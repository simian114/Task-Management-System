package taskmanagement.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taskmanagement.domain.User;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 회원_저장_성공() {
        // given
        User user = new User();
        user.setEmail("hello@gmail.com");
        user.setPassword("123456");

        // when
        User saveUser = this.userRepository.save(user);

        // then
        User findUser = this.userRepository.findById(saveUser.getUserId()).get();
        Assertions.assertThat(findUser).isSameAs(saveUser);
    }

    @Test
    public void 오류_회원가입시_비밀번호_필수() {
         // given
        User user = new User();
        user.setEmail("hello@gmail.com");

        // when, then
        assertThrows(Exception.class, () -> this.userRepository.save(user));
    }

    @Test
    public void 오류_이메일_중복_안됨() {
        // given
        User user = new User();
        User user2 = new User();
        user.setEmail("hello@gmail.com");
        user.setPassword("123456");
        user2.setEmail("hello@gmail.com");
        user2.setPassword("123456");
        this.userRepository.save(user);

        // when, then
        assertThrows(Exception.class, () -> this.userRepository.save(user2));
    }
}