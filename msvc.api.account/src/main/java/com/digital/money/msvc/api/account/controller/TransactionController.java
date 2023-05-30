package com.digital.money.msvc.api.account.controller;

import com.digital.money.msvc.api.account.model.dto.TransactionPostDto;
import com.digital.money.msvc.api.account.service.impl.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "Save an transaction", hidden = true)
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody TransactionPostDto transactionPostDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.save(transactionPostDto));
    }

//    @GetMapping
//    public List<TransactionGetDto> getLastFive(@RequestParam Long id) throws ResourceNotFoundException {
//        return transactionService.getLastFive(id);
//    }
}
