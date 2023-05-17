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
import pyankovdaniil.microservices.dto.RemoveCardRequest;

@RestController
@RequestMapping("/api/v1/cards")
@Slf4j
public record CardController(
        AuthenticationClient authenticationClient,
        CardsService cardsService) {
    private static final String AUTH_HEADER_NAME = "Authorization";

    @PostMapping("/add-card")
    public ResponseEntity<?> addCard(HttpServletRequest request, @RequestBody AddCardRequest addCardRequest) {
        String bearerTokenHeader = request.getHeader(AUTH_HEADER_NAME);
        BearerToken bearerToken = BearerToken.builder().bearerToken(bearerTokenHeader).build();
        ResponseEntity<ExtractedEmail> response = authenticationClient.extractEmail(bearerToken);
        if (response.getStatusCode().equals(HttpStatusCode.valueOf(HttpStatus.OK.value()))) {
            ExtractedEmail extractedEmail = response.getBody();
            if (extractedEmail != null) {
                return cardsService.addCard(extractedEmail, addCardRequest);
            }
        }

        return new ResponseEntity<>("Invalid Bearer token or request body!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/remove-card")
    public ResponseEntity<?> removeCard(HttpServletRequest request, @RequestBody RemoveCardRequest removeCardRequest) {
        String bearerTokenHeader = request.getHeader(AUTH_HEADER_NAME);
        BearerToken bearerToken = BearerToken.builder().bearerToken(bearerTokenHeader).build();
        ResponseEntity<ExtractedEmail> response = authenticationClient.extractEmail(bearerToken);
        if (response.getStatusCode().equals(HttpStatusCode.valueOf(HttpStatus.OK.value()))) {
            ExtractedEmail extractedEmail = response.getBody();
            if (extractedEmail != null) {
                return cardsService.removeCard(extractedEmail, removeCardRequest);
            }
        }

        return new ResponseEntity<>("Invalid Bearer token or request body!", HttpStatus.BAD_REQUEST);
    }
}
