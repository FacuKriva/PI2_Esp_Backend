package com.digital.money.msvc.api.account.controller;

import com.digital.money.msvc.api.account.handler.*;
import com.digital.money.msvc.api.account.model.dto.AliasUpdate;
import com.digital.money.msvc.api.account.model.dto.CardPostDTO;
import com.digital.money.msvc.api.account.model.dto.CardTransactionPostDTO;
import com.digital.money.msvc.api.account.service.impl.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @Operation(summary = "Find an account by id")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @Operation(summary = "Find all transactions by account id")
    @GetMapping("/{id}/transactions")
    public ResponseEntity<Object> findAllByAccountId(@PathVariable(name = "id") Long account_id) throws ResourceNotFoundException {
        if (accountService.findAllByAccountId(account_id).getTransactions().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("The account doesn't have any transactions");
        }
        return ResponseEntity.ok(accountService.findAllByAccountId(account_id));
    }

    @Operation(summary = "Save an account", hidden = true)
    @PostMapping
    public ResponseEntity<Object> save(@RequestParam(name = "user_id") Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.save(userId));
    }

    @Operation(summary = "Update account alias")
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateAlias(@PathVariable(name = "id") Long id,
                                              @RequestBody AliasUpdate aliasUpdate) throws AlreadyRegisteredException, ResourceNotFoundException, BadRequestException {
        String response = accountService.updateAlias(id, aliasUpdate);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add a card to an account")
    @PostMapping(value = "/{id}/cards", consumes = "application/json")
    public ResponseEntity<?> addCard(@PathVariable(name = "id") Long id
                                    , @Valid @RequestBody CardPostDTO cardPostDTO)
            throws ResourceNotFoundException, AlreadyRegisteredException, BadRequestException {

        return ResponseEntity.ok(accountService.addCard(id, cardPostDTO));
    }

    @Operation(summary = "List all cards from an account")
    @GetMapping(value = "/{id}/cards", produces = "application/json")
    public ResponseEntity<?> listCards(@PathVariable(name = "id") Long id) throws ResourceNotFoundException {
        if (accountService.listAllCards(id).isEmpty()) {
            return new ResponseEntity("The are no cards associated with this account"
                    , HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(accountService.listAllCards(id));
        }
    }

    @Operation(summary = "Find a card by id")
    @GetMapping(value = "/{id}/cards/{cardId}", produces = "application/json")
    public ResponseEntity<?> findCardById(@PathVariable(name = "id") Long id, @PathVariable(name = "cardId") Long cardId) throws ResourceNotFoundException {
        return ResponseEntity.ok(accountService.findCardFromAccount(id, cardId));
    }

    @Operation(summary = "Delete a card from an account")
    @DeleteMapping(value = "/{id}/cards/{cardId}", produces = "application/json")
    public ResponseEntity<?> deleteCard(@PathVariable(name = "id") Long id, @PathVariable(name = "cardId") Long cardId) throws ResourceNotFoundException {
        String cardNumber = accountService.findCardFromAccount(id, cardId).getCardNumber();
        accountService.removeCardFromAccount(id, cardId);
        return ResponseEntity.ok("The card N°" + cardNumber + " has been successfully removed from your " +
                "account");
    }

    @Operation(summary = "Deposit money into account from card")
    @PostMapping(value = "/{id}/transferences/deposit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> deposit(@PathVariable("id") Long id,
                                          @Valid @RequestBody CardTransactionPostDTO cardTransactionPostDTO) throws PaymentRequiredException, UnauthorizedException, ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.depositMoney(id, cardTransactionPostDTO));
    }

}
