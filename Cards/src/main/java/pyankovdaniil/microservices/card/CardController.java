package pyankovdaniil.microservices.card;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pyankovdaniil.microservices.clients.authentication.AuthenticationClient;
import pyankovdaniil.microservices.clients.authentication.dto.BearerToken;
import pyankovdaniil.microservices.clients.authentication.dto.ExtractedEmail;
import pyankovdaniil.microservices.dto.AddCardRequest;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/cards")
@Slf4j
public record CardController(
        AuthenticationClient authenticationClient,
        CardRepository cardRepository) {
    private static final String AUTH_HEADER_NAME = "Authorization";

    @PostMapping("/add-card")
    public ResponseEntity<?> addCard(HttpServletRequest request, @RequestBody AddCardRequest addCardRequest) {
        String bearerTokenHeader = request.getHeader(AUTH_HEADER_NAME);
        BearerToken bearerToken = BearerToken.builder().bearerToken(bearerTokenHeader).build();

        ResponseEntity<ExtractedEmail> response = authenticationClient.extractEmail(bearerToken);
        if (response.getStatusCode().equals(HttpStatusCode.valueOf(HttpStatus.OK.value()))) {
            ExtractedEmail extractedEmail = response.getBody();
            log.info("Got /add-card request with ownerEmail: {} and cardName: {}", extractedEmail.getEmail(), addCardRequest.getCardName());
            Card cardToInsert = Card.builder()
                    .ownerEmail(extractedEmail.getEmail())
                    .cardName(addCardRequest.getCardName())
                    .balance(BigDecimal.ZERO)
                    .build();
            cardRepository.save(cardToInsert);
            return ResponseEntity.ok(addCardRequest.getCardName());
        }

        return new ResponseEntity<>("...", HttpStatus.BAD_REQUEST);
    }
}
