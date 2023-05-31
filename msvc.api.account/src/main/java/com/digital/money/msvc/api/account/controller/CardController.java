package com.digital.money.msvc.api.account.controller;

import com.digital.money.msvc.api.account.handler.*;
import com.digital.money.msvc.api.account.model.dto.CardPostDTO;
import com.digital.money.msvc.api.account.service.impl.CardService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @Operation(summary = "Add a card to an account")
    @PostMapping(value = "{id}/cards/add-card", consumes = "application/json")
    public ResponseEntity<?> addCard(@PathVariable(name = "id") Long id, @RequestBody CardPostDTO cardPostDTO) throws ResourceNotFoundException, CardAlreadyExistsException {
        return ResponseEntity.ok(cardService.addCardToAccount(id, cardPostDTO));
    }

    @Operation(summary = "List all cards from an account")
    @GetMapping(value = "{id}/cards/list-all", produces = "application/json")
    public ResponseEntity<?> listCards(@PathVariable(name = "id") Long id) throws ResourceNotFoundException {
        if (cardService.listCardsFromAccount(id).isEmpty()) {
            return new ResponseEntity("The are no cards associated with this account"
                    , HttpStatus.OK);
        } else {
            return ResponseEntity.ok(cardService.listCardsFromAccount(id));
        }
    }

    @Operation(summary = "Find a card by id")
    @GetMapping(value = "{id}/cards/{cardId}", produces = "application/json")
    public ResponseEntity<?> findCardById(@PathVariable(name = "id") Long id, @PathVariable(name = "cardId") Long cardId) throws ResourceNotFoundException, CardNotFoundException {
        return ResponseEntity.ok(cardService.findCardFromAccount(id, cardId));
    }

    @Operation(summary = "Delete a card from an account")
    @DeleteMapping(value = "{id}/cards/remove/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable(name = "id") Long id, @PathVariable(name = "cardId") Long cardId) throws ResourceNotFoundException, CardNotFoundException {
        cardService.removeCardFromAccount(id, cardId);
        return ResponseEntity.ok("Card successfully removed from account");
    }
}
