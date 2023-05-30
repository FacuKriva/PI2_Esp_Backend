package com.digital.money.msvc.api.users.controllers;

import com.digital.money.msvc.api.users.controllers.requestDto.CardRequestDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.NewPassDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.VerficationRequestDTO;
import com.digital.money.msvc.api.users.dtos.CardDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;
import com.digital.money.msvc.api.users.entities.Card;
import com.digital.money.msvc.api.users.exceptions.*;
import com.digital.money.msvc.api.users.services.impl.UserService;
import jakarta.validation.Valid;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        public ResponseEntity<?> resendVerificationMail(@RequestHeader("Authorization") String token) throws Exception {
        userService.resendVerificationMail(token);
        return ResponseEntity.ok("Please check your inbox. You will receive an email with a new verification code");
    }

    @PutMapping("/verificate")
    public ResponseEntity<?> verificateCode(@RequestBody VerficationRequestDTO verficationRequestDTO, @RequestHeader("Authorization") String token) throws JSONException {
        return userService.verificateUser(verficationRequestDTO, token);
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) throws UserNotFoundException {

        userService.forgotPassword(email);
        return ResponseEntity.ok("Please check your inbox. You will receive an email with a link to reset your password");
    }

    @PutMapping("/reset-password/{recoveryCode}")
    public ResponseEntity<?> resetPassword(
            @PathVariable("recoveryCode") String recoveryCode,
            @RequestBody NewPassDTO passwords)
            throws PasswordNotChangedException {

        userService.resetPassword(recoveryCode, passwords);
        return ResponseEntity.ok("Your password has been successfully updated");
    }

    @PostMapping(value = "/{dni}/add-card", consumes = "application/json")
    public ResponseEntity<?> addCardToAccount(@PathVariable("dni") Long dni, @RequestBody CardRequestDTO cardRequestDTO) throws UserNotFoundException, CardAlreadyExistsException {
        if (userService.doesCardExist(cardRequestDTO.getCardNumber())) {
            return new ResponseEntity(HttpStatus.CONFLICT).ok("Card already exists and is associated with another account");
        }
        else {
            userService.addCardToAccount(dni, cardRequestDTO);
            return new ResponseEntity(HttpStatus.CREATED).ok("Card successfully added to account");
        }
    }

    @PutMapping("/{dni}/remove-card/{cardId}")
    public ResponseEntity<?> removeCardFromAccount(@PathVariable("dni") Long dni, @PathVariable("cardId") Long cardId) throws UserNotFoundException, CardNotFoundException {
        userService.removeCardFromAccount(dni, cardId);
        return new ResponseEntity(HttpStatus.OK).ok("Card successfully removed from account");
    }

    @GetMapping(value = "/{dni}/cards", produces = "application/json")
    public ResponseEntity<List<CardDTO>> getAllCardsFromAccount(@PathVariable("dni") Long dni) throws UserNotFoundException, NoCardsException {
        List<CardDTO> cardDTOs = userService.getAllCardsFromAccount(dni);
        return ResponseEntity.ok(cardDTOs);
    }

    @GetMapping(value = "/{dni}/cards/{cardId}", produces = "application/json")
    public ResponseEntity<?> getCardFromAccount(@PathVariable("dni") Long dni, @PathVariable("cardId") Long cardId) throws UserNotFoundException, CardNotFoundException {
        return ResponseEntity.ok(userService.getCardFromAccount(dni, cardId));
    }
}