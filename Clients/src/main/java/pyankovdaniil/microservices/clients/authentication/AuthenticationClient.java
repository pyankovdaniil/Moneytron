package pyankovdaniil.microservices.clients.authentication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pyankovdaniil.microservices.clients.authentication.dto.BearerToken;
import pyankovdaniil.microservices.clients.authentication.dto.ExtractedEmail;

@FeignClient("Authentication")
public interface AuthenticationClient {
    @PostMapping("/api/v1/auth/extract-email")
    ResponseEntity<ExtractedEmail> extractEmail(@RequestBody BearerToken bearerToken);
}
