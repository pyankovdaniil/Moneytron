package pyankovdaniil.microservices.cards;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pyankovdaniil.microservices.clients.auth.AuthenticationClient;

@RestController
@RequestMapping("/api/v1/cards")
@Slf4j
public record CardsController(AuthenticationClient authenticationClient) {
    @GetMapping("/get-card")
    public String getCard() {
        log.info("Received /get-card request");
        return "You receive a card and token: " + authenticationClient.getJwt();
    }
}
