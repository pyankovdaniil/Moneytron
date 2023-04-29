package pyankovdaniil.microservices.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pyankovdaniil.microservices.clients.authentication.dto.BearerToken;
import pyankovdaniil.microservices.dto.AuthenticationRequest;
import pyankovdaniil.microservices.clients.authentication.dto.ExtractedEmail;
import pyankovdaniil.microservices.dto.Message;
import pyankovdaniil.microservices.dto.tokens.RefreshJwtRequest;
import pyankovdaniil.microservices.dto.RegistrationRequest;
import pyankovdaniil.microservices.dto.tokens.RefreshJwtResponse;
import pyankovdaniil.microservices.dto.tokens.TokensResponse;
import pyankovdaniil.microservices.jwt.JwtService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public record AuthenticationController(
        AuthenticationService authenticationService,
        JwtService jwtService
) {
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
        log.info("Got registration request for user with email {}", registrationRequest.getEmail());
        Optional<TokensResponse> tokensResponse = authenticationService.register(registrationRequest);
        if (tokensResponse.isPresent()) {
            return ResponseEntity.ok(tokensResponse);
        }

        Message response = Message.builder().message("User with this username already exists!").build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Got authentication request for user with email {}", authenticationRequest.getEmail());
        Optional<TokensResponse> tokensResponse = authenticationService.authenticate(authenticationRequest);
        if (tokensResponse.isPresent()) {
            return ResponseEntity.ok(tokensResponse);
        }

        Message response = Message.builder().message("Invalid email and password!").build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwt(@RequestBody RefreshJwtRequest refreshJwtRequest) {
        log.info("Got refresh token request for user with token {}", refreshJwtRequest.getRefreshToken());
        Optional<RefreshJwtResponse> refreshJwtResponse = authenticationService.refresh(refreshJwtRequest);
        if (refreshJwtResponse.isPresent()) {
            return ResponseEntity.ok(refreshJwtResponse);
        }

        Message response = Message.builder().message("Invalid refresh token!").build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/extract-email")
    public ResponseEntity<?> extractEmail(@RequestBody BearerToken bearerToken) {
        Optional<String> email = authenticationService.validateBearerToken(bearerToken);
        if (email.isPresent()) {
            log.info("Successfully extracted email: {}", email.get());
            return ResponseEntity.ok(ExtractedEmail.builder().email(email.get()).build());
        }

        Message response = Message.builder().message("Invalid bearer authentication token!").build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
