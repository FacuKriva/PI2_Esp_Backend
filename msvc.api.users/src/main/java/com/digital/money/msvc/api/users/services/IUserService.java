package com.digital.money.msvc.api.users.services;

import com.digital.money.msvc.api.users.controllers.requestDto.NewPassDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.VerficationRequestDTO;
import com.digital.money.msvc.api.users.dtos.AuthUserDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;
import com.digital.money.msvc.api.users.exceptions.PasswordNotChangedException;
import com.digital.money.msvc.api.users.exceptions.UserNotFoundException;
import jakarta.ws.rs.BadRequestException;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;

public interface IUserService {

    UserDTO createUser(UserRequestDTO userRequestDTO) throws Exception;
    AuthUserDTO getUserByEmail(String email) throws UserNotFoundException;
    UserDTO getUserByDni(Long dni) throws UserNotFoundException;
    void updateAttempsFromUser(Long userId, boolean enabled, int attempts) throws UserNotFoundException;

    void sendVerificationMail(String email);
    void resendVerificationMail(String token) throws JSONException, BadRequestException;
    ResponseEntity<String> verificateUser(VerficationRequestDTO verficationRequestDTO, String token) throws JSONException;

    void forgotPassword(String email) throws UserNotFoundException;
    void resetPassword(String recoveryCode, NewPassDTO passwords) throws PasswordNotChangedException;
}
