package taskmanagement.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taskmanagement.service.UserService;
import taskmanagement.web.dto.UserDto;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AccountController {
    private final UserService userService;

    @PostMapping("/accounts")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto
    ) {
        this.userService.createUser(userDto);
        return ResponseEntity.ok().build();
    }
}
