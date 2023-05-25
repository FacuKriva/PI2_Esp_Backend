package com.digital.money.msvc.api.users.clients;

import com.digital.money.msvc.api.users.clients.dtos.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "msvc-account-api")
public interface IAccountClient {

    @PostMapping("/create/account/{user_id}")
    Map<String, Integer> createUserAccount(@PathVariable(name = "user_id") Long userId,
                                           @RequestBody AccountDTO accountDto);
}
