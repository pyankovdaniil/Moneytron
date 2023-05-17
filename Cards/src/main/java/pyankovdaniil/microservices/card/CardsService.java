package pyankovdaniil.microservices.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pyankovdaniil.microservices.clients.authentication.dto.ExtractedEmail;
import pyankovdaniil.microservices.dto.AddCardRequest;
import pyankovdaniil.microservices.dto.RemoveCardRequest;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardsService {
    private final CardRepository cardRepository;

    public ResponseEntity<?> addCard(ExtractedEmail extractedEmail, AddCardRequest addCardRequest) {
        log.info("Got /add-card request with ownerEmail: {} and cardName: {}", extractedEmail.getEmail(), addCardRequest.getCardName());
        Card cardToInsert = Card.builder()
                .ownerEmail(extractedEmail.getEmail())
                .cardName(addCardRequest.getCardName())
                .balance(BigDecimal.ZERO)
                .build();
        cardRepository.save(cardToInsert);
        return ResponseEntity.ok(addCardRequest.getCardName());
    }

    public ResponseEntity<?> removeCard(ExtractedEmail extractedEmail, RemoveCardRequest removeCardRequest) {
        log.info("Got /remove-card request with ownerEmail: {} and cardName: {}", extractedEmail.getEmail(), removeCardRequest.getCardName());
        Card cardToInsert = Card.builder()
                .ownerEmail(extractedEmail.getEmail())
                .cardName(removeCardRequest.getCardName())
                .balance(BigDecimal.ZERO)
                .build();
        cardRepository.delete(cardToInsert);
        // TODO: update all transactions of that card in Transactions database
        return ResponseEntity.ok(removeCardRequest.getCardName());
    }
}
