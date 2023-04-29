package pyankovdaniil.microservices.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pyankovdaniil.microservices.clients.authentication.dto.BearerToken;
import pyankovdaniil.microservices.dto.AuthenticationRequest;
import pyankovdaniil.microservices.dto.RegistrationRequest;
import pyankovdaniil.microservices.dto.tokens.RefreshJwtRequest;
import pyankovdaniil.microservices.dto.tokens.RefreshJwtResponse;
import pyankovdaniil.microservices.dto.tokens.TokensResponse;
import pyankovdaniil.microservices.jwt.JwtAuthenticationFilter;
import pyankovdaniil.microservices.jwt.JwtService;
import pyankovdaniil.microservices.user.User;
import pyankovdaniil.microservices.user.UserRepository;
import pyankovdaniil.microservices.user.UserRole;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public Optional<TokensResponse> register(RegistrationRequest registrationRequest) {
        User user = User.builder()
                .fullName(registrationRequest.getFullName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .role(UserRole.USER)
                .build();

        Optional<User> userInDatabase = userRepository.findUserByEmail(user.getEmail());
        if (userInDatabase.isPresent()) {
            return Optional.empty();
        }

        userRepository.save(user);
        return Optional.of(generateTokensForUser(user));
    }

    private TokensResponse generateTokensForUser(User user) {
        String refreshToken = jwtService.generateRefreshToken(user);
        redisTemplate.opsForValue().set(refreshToken, user.getEmail(),
                Duration.ofMillis(JwtService.REFRESH_TOKEN_EXPIRE_TIME_MS));

        return TokensResponse
                .builder()
                .accessToken(jwtService.generateJwt(user))
                .refreshToken(refreshToken)
                .build();
    }

    public Optional<TokensResponse> authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()
        ));

        return userRepository.findUserByEmail(authenticationRequest.getEmail())
                .map(this::generateTokensForUser);
    }

    public Optional<RefreshJwtResponse> refresh(RefreshJwtRequest refreshJwtRequest) {
        String email;
        if ((email = redisTemplate.opsForValue().get(refreshJwtRequest.getRefreshToken())) != null) {
            Optional<User> user = userRepository.findUserByEmail(email);
            if (user.isPresent()) {
                return Optional.of(RefreshJwtResponse.builder()
                        .newJwt(jwtService.generateJwt(user.get()))
                        .build());
            }
        }

        return Optional.empty();
    }

    public Optional<String> validateBearerToken(BearerToken bearerToken) {
        String tokenWithPrefix = bearerToken.getBearerToken();
        if (tokenWithPrefix.startsWith(JwtAuthenticationFilter.AUTH_HEADER_START)) {
            String token = tokenWithPrefix.substring(JwtAuthenticationFilter.JWT_START_POSITION);
            return jwtService.extractEmail(token);
        }

        return Optional.empty();
    }
}
