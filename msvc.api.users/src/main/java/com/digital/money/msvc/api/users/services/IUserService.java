package com.digital.money.msvc.api.users.services;

import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;
import com.digital.money.msvc.api.users.exceptions.UserNotFoundException;

public interface IUserService {

    UserDTO createUser(UserRequestDTO userRequestDTO) throws Exception;
    UserDTO getUserByEmail(String email) throws UserNotFoundException;
    UserDTO getUserByDni(Long dni) throws UserNotFoundException;
    void updateAttempsFromUser(Long userId, boolean enabled, int attempts) throws UserNotFoundException;
}
