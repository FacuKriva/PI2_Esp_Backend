package com.digital.money.msvc.api.users.services;

import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.VerficationRequestDTO;
import com.digital.money.msvc.api.users.dtos.AuthUserDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;
import com.digital.money.msvc.api.users.exceptions.UserNotFoundException;

public interface IUserService {

    UserDTO createUser(UserRequestDTO userRequestDTO) throws Exception;
    AuthUserDTO getUserByEmail(String email) throws UserNotFoundException;
    UserDTO getUserByDni(Long dni) throws UserNotFoundException;
    void updateAttempsFromUser(Long userId, boolean enabled, int attempts) throws UserNotFoundException;

   void resendVerificationMail(String token) throws JSONException;
    String verificateUser(VerficationRequestDTO verficationRequestDTO, String token) throws JSONException;

    void forgotPassword(String email);

    void resetPassword(String email, String password) throws PasswordNotChangedException;
}
