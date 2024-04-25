package taskmanagement.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import taskmanagement.domain.User;
import taskmanagement.web.dto.UserDto;

@RequiredArgsConstructor
@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        return user;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}
