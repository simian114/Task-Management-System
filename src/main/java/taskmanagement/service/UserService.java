package taskmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taskmanagement.domain.User;
import taskmanagement.repository.UserRepository;
import taskmanagement.web.dto.UserDto;
import taskmanagement.web.exception.exceptions.conflict.ConflictException;
import taskmanagement.web.mapper.UserMapper;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public void createUser(UserDto userDto) throws ConflictException {
        // check exists
        this.userRepository.findByEmailIgnoreCase(userDto.getEmail()).ifPresent(u -> {
            throw new ConflictException("User already exists");
        });
        User user = this.userMapper.toEntity(userDto);
        this.userMapper.toDto(this.userRepository.save(user));
    }
}
