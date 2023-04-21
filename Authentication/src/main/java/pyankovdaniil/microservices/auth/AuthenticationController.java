package pyankovdaniil.microservices.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public record AuthenticationController() {
    @GetMapping("/get-jwt")
    public String getJwt() {
        return "Your-256bit-JWT";
    }
}
