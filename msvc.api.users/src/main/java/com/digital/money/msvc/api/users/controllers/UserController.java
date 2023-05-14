package com.digital.money.msvc.api.users.controllers;

import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;
import com.digital.money.msvc.api.users.exceptions.HasAlreadyBeenRegistred;
import com.digital.money.msvc.api.users.exceptions.UserNotFoundException;
import com.digital.money.msvc.api.users.services.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        UserDTO userDTO = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @GetMapping("/email")
    public ResponseEntity<?> findByEmail(@RequestParam("email") String email) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
}
