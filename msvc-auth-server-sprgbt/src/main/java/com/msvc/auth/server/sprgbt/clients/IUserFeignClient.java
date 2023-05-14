package com.msvc.auth.server.sprgbt.clients;

import com.msvc.auth.server.sprgbt.clients.request.UserRequestDTO;
import com.msvc.auth.server.sprgbt.dtos.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "msvc-users-api")
public interface IUserFeignClient {

    @PutMapping("/users/update/attempts/{user_id}")
    void updateUserAttempts(@PathVariable("user_id") Long userId, @RequestBody UserRequestDTO userDto);

    @GetMapping("/users/email")
    UserDTO findByEmail(@RequestParam String email);
}
