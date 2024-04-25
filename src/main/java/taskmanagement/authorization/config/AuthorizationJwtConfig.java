package taskmanagement.authorization.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import taskmanagement.authorization.service.UserDetailsServiceImpl;
import taskmanagement.repository.UserRepository;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * 보통은 access token 을 발급해주는 Authorization 서버가 따로 존재한다.
 * 하지만, 이건 여기서는 Resource 서버가 Authorization 서버도 겸한다.
 * 따라서 Authorization 을 담당하는 관련 설정을 만들어야한다.

 * Authorization 서버의 역할..
 * 1. 비대칭 키 중 private key 를 가지고 있는다.
 * 2. access token 을 발급해준다.
 * --> encoder 를 구성해야한다.

 * 반면 Resource 서버의 역할...
 * 1. 접근 제한이 걸려 있는 Resource 를 가지고 있는다.
 * 2. bearer token 을 검증한다.
 * --> decoder 를 구성해야 한다.
 */
@RequiredArgsConstructor
@Configuration
public class AuthorizationJwtConfig {
    private final UserRepository userRepository;

    @Bean
    public KeyPair generateRsaKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            // exception handling...
            throw new RuntimeException(e);
        }
    }

    @Bean
    JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    UserDetailsService userDetailsService() {
        // UserDetailsServiceImpl에 @Component를 작성하지 않고, 이렇게 Bean으로 등록해도될듯
        return new UserDetailsServiceImpl(userRepository);
    }


    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

}
