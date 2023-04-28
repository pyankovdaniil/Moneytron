package pyankovdaniil.microservices.cards;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pyankovdaniil.microservices.clients.auth.AuthenticationClient;

@RestController
@RequestMapping("/api/v1/cards")
@Slf4j
@Tag(name = "Cards Controller", description = "Microservice RESTful APIs")
public record CardsController(AuthenticationClient authenticationClient) {
    @GetMapping("/get-card")
    @Operation(
            summary = "Get a card",
            description = "Get a card and a token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = String.class), mediaType = "application/json")
            })
    })
    public String getCard() {
        log.info("Received /get-card request");
        return "You receive a card and token: " + authenticationClient.getJwt();
    }
}
