package com.digital.money.msvc.api.users.clients;

import com.digital.money.msvc.api.users.clients.dtos.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "msvc-account-api")
public interface IAccountClient {

    @PostMapping("/accounts")
    AccountDTO createAccount(@RequestParam(name = "user_id") Long userId);

    @GetMapping("/{id}")
    AccountDTO getAccountById(@PathVariable(name = "id") Integer accountId);

}
