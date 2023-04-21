package pyankovdaniil.microservices.cards;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pyankovdaniil.microservices.clients.auth.AuthenticationClient;

@RestController
@RequestMapping("/api/v1/cards")
public record CardsController(AuthenticationClient authenticationClient) {
    @GetMapping("/get-card")
    public String getCard() {
        return "You receive a card and token: " + authenticationClient.getJwt();
    }
}
