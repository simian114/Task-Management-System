package taskmanagement.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taskmanagement.authorization.constants.AuthorizationScheme;
import taskmanagement.authorization.service.TokenService;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;

    @PostMapping("/auth/token")
    public ResponseEntity<?> login(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(AuthorizationScheme.BASIC)) {
            throw new BadCredentialsException("Should have Authorization header with Basic auth scheme");
        }

        return ResponseEntity.ok(Map.of("token", this.tokenService.generateToken(authHeader)));
    }
}
