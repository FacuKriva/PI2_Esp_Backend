package com.msvc.auth.server.sprgbt.services;

import com.msvc.auth.server.sprgbt.clients.request.UserRequestDTO;
import com.msvc.auth.server.sprgbt.dtos.UserDTO;

public interface IUserService {

    UserDTO findUseByEmail(String email);

    void updateUser(Long userId, UserRequestDTO userDto);
}
