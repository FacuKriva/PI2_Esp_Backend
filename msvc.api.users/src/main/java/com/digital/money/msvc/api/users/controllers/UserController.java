package com.digital.money.msvc.api.users.controllers;

import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.exceptions.HasAlreadyBeenRegistred;
import com.digital.money.msvc.api.users.services.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registrar Usuario
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) throws Exception {
        if (Objects.isNull(userRequestDTO)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "user created successfully"));
    }
}
