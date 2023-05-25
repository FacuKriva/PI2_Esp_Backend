package com.digital.money.msvc.api.users.controllers;

import com.digital.money.msvc.api.users.controllers.requestDto.NewPassDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.CreateUserRequestDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.UpdateUserRequestDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.VerficationRequestDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;
import com.digital.money.msvc.api.users.exceptions.PasswordNotChangedException;
import com.digital.money.msvc.api.users.exceptions.UserNotFoundException;
import com.digital.money.msvc.api.users.services.impl.UserService;
import jakarta.validation.Valid;
import org.json.JSONException;
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
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDTO userRequestDTO) throws Exception {
        if (Objects.isNull(userRequestDTO)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        UserDTO userDTO = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    /**
     * Actualizar informacion del usuario.
     */
    @PatchMapping("/update/{user_id}")
    public ResponseEntity<?> updateUser(@PathVariable("user_id") Long userId,
                                        @Valid @RequestBody final UpdateUserRequestDTO userDto) throws UserNotFoundException {

        if (Objects.isNull(userDto)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        userService.update(userDto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "user updated successfully"));
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
    public ResponseEntity<?> verificateCode(@RequestBody VerficationRequestDTO verficationRequestDTO,
                                            @RequestHeader("Authorization") String token) throws JSONException {
        return userService.verificateUser(verficationRequestDTO, token);
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {

        userService.forgotPassword(email);
        return ResponseEntity.ok("Se ha enviado un correo para cambiar la contraseña");
    }

    @PutMapping("/reset-password/{recoveryCode}")
    public ResponseEntity<?> resetPassword(
            @PathVariable("recoveryCode") String recoveryCode,
            @RequestBody NewPassDTO passwords)
            throws PasswordNotChangedException {

        userService.resetPassword(recoveryCode, passwords);
        return ResponseEntity.ok("Contraseña cambiada correctamente");
    }
}
