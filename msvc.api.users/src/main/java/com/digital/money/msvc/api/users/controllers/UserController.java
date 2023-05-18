package com.digital.money.msvc.api.users.controllers;

import com.digital.money.msvc.api.users.controllers.requestDto.ResendCodeDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.VerficationRequestDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;
import com.digital.money.msvc.api.users.exceptions.UserNotFoundException;
import com.digital.money.msvc.api.users.services.impl.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.QueryParam;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        System.out.println("hola");

        UserDTO userDTO = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @GetMapping("/dni")
    public ResponseEntity<?> findByDni(@RequestParam("dni") Long dni) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUserByDni(dni));
    }

    @GetMapping("/email")
    public ResponseEntity<?> findByEmail(@RequestParam("email") String email) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping("/update/attempts/{dni}")
    public ResponseEntity<?> updateUserAttempts(@PathVariable Long dni,
                                                @RequestParam("enabled") boolean enabled,
                                                @RequestParam("attempts") int attempts) throws UserNotFoundException {

        userService.updateAttempsFromUser(dni, enabled, attempts);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/resend")
    public ResponseEntity<?> resendVerificationMail(@RequestHeader("Authorization") String token) throws JSONException {

        userService.resendVerificationMail(token);
        return ResponseEntity.ok("Mail enviado correctamente");
    }

    @PutMapping("/verificate")
    public ResponseEntity<?> verificateCode(@RequestBody VerficationRequestDTO verficationRequestDTO, @RequestHeader("Authorization") String token) throws JSONException {

        userService.verificateUser(verficationRequestDTO, token);
        return ResponseEntity.ok("Mail verificado correctamente");
    }


}
