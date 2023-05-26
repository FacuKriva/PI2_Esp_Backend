package com.digital.money.msvc.api.account.controller;

import com.digital.money.msvc.api.account.handler.AlreadyRegisteredException;
import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
import com.digital.money.msvc.api.account.model.dto.AccountPostDto;
import com.digital.money.msvc.api.account.model.dto.AliasUpdate;
import com.digital.money.msvc.api.account.service.impl.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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
    public ResponseEntity<Object> findAllByAccountId(@PathVariable Long account_id) throws ResourceNotFoundException {
        return ResponseEntity.ok(accountService.findAllByAccountId(account_id));
    }

    @Operation(summary = "Save an account", hidden = true)
    @PostMapping
    public ResponseEntity<Object> save(@RequestParam(name = "user_id") Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.save(userId));
    }

    @Operation(summary = "Update account alias")
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateAlias(@PathVariable Long id, @RequestBody AliasUpdate aliasUpdate) throws AlreadyRegisteredException, ResourceNotFoundException {
        accountService.updateAlias(id, aliasUpdate);
        return ResponseEntity.ok("New Alias: " + aliasUpdate);
    }
}