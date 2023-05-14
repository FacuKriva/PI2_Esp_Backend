package com.digital.money.msvc.api.users.services;

import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;

public interface IUserService {

    UserDTO createUser(UserRequestDTO userRequestDTO) throws Exception;
}
