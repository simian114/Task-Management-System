package taskmanagement.authorization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import taskmanagement.authorization.adapter.UserAdapter;
import taskmanagement.authorization.constants.AuthorizationScheme;
import taskmanagement.domain.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final AuthenticationManager authenticationManager;

    public String generateToken(String authHeader) {
        String encoded = authHeader.split(AuthorizationScheme.BASIC)[1];
        String decoded = new String(Base64.getDecoder().decode(encoded));
        String[] credentials = decoded.split(":");

        Authentication authToken = new UsernamePasswordAuthenticationToken(credentials[0], credentials[1]);

        Authentication authentication = authenticationManager.authenticate(authToken);

        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        User user = ((UserAdapter) authentication.getPrincipal()).getUser();

        int EXPIRATION_TIME = 3600;

        JwtClaimsSet claimSet = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(EXPIRATION_TIME, ChronoUnit.SECONDS))
                .claim("scope", authorities)
                .claim("id", user.getUserId())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
    }
}
