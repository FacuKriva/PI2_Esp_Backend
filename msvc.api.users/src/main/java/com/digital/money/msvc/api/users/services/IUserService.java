package com.digital.money.msvc.api.users.services;

import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.exceptions.HasAlreadyBeenRegistred;

public interface IUserService {

    void createUser(UserRequestDTO userRequestDTO) throws Exception;
}
