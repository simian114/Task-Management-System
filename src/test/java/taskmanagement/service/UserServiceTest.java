package taskmanagement.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taskmanagement.web.dto.UserDto;
import taskmanagement.web.exception.exceptions.conflict.ConflictException;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void 유저_생성_성공() {
        // given
        UserDto userDto = new UserDto();
        userDto.setEmail("hello@gmail.com");
        userDto.setPassword("123456");

        // when
        userService.createUser(userDto);
    }

    @Test
    void 실패_이메일_중복_409() {
        // given
        UserDto userDto = new UserDto();
        userDto.setEmail("hello@gmail.com");
        userDto.setPassword("123456");
        userService.createUser(userDto);

        //when, then
        Assertions.assertThrows(ConflictException.class, () -> userService.createUser(userDto));
    }

}