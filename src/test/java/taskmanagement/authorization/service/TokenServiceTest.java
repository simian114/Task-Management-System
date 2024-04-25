package taskmanagement.authorization.service;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import taskmanagement.domain.User;
import taskmanagement.repository.UserRepository;
import taskmanagement.service.UserService;
import taskmanagement.web.dto.UserDto;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class TokenServiceTest {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtDecoder jwtDecoder;

    @Test
    void 성공_jwt토큰_생성() {
        // given
        UserDto userDto = new UserDto();
        userDto.setEmail("hello@gmail.com");
        userDto.setPassword("password");
        this.userService.createUser(userDto);

        byte[] bytes = (userDto.getEmail() +
                ":" +
                userDto.getPassword()).getBytes();

        Base64.Encoder encoder = Base64.getEncoder();
        String base64Encoded = encoder.encodeToString(bytes);

        // when
        User user = this.userRepository.findByEmailIgnoreCase("hello@gmail.com").get();
        String token = this.tokenService.generateToken("Basic " + base64Encoded);
        Jwt jwt = jwtDecoder.decode(token);

        // then
        Assertions.assertThat((long) jwt.getClaim("id")).isEqualTo(user.getUserId());
    }

    @Test
    void 실패_계정없음() {
        // given
        UserDto userDto = new UserDto();
        userDto.setEmail("hello@gmail.com");
        userDto.setPassword("password");
        this.userService.createUser(userDto);

        byte[] bytes = ("helloworld@gmail.com" +
                ":" +
                "password").getBytes();

        Base64.Encoder encoder = Base64.getEncoder();
        String base64Encoded = encoder.encodeToString(bytes);

        // then
        assertThrows(BadCredentialsException.class, () -> this.tokenService.generateToken("Basic " + base64Encoded));
    }


    @Test
    void 실패_패스워드_오류() {
        // given
        UserDto userDto = new UserDto();
        userDto.setEmail("hello@gmail.com");
        userDto.setPassword("password");
        this.userService.createUser(userDto);

        byte[] bytes = (userDto.getEmail() +
                ":" +
                "helloworld").getBytes();

        Base64.Encoder encoder = Base64.getEncoder();
        String base64Encoded = encoder.encodeToString(bytes);

        // then
        assertThrows(BadCredentialsException.class, () -> this.tokenService.generateToken("Basic " + base64Encoded));
    }

}