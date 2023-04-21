package pyankovdaniil.microservices.clients.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("Authentication")
public interface AuthenticationClient {
    @GetMapping("/api/v1/auth/get-jwt")
    String getJwt();
}
