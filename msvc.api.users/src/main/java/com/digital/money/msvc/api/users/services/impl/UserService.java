package com.digital.money.msvc.api.users.services.impl;

import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;
import com.digital.money.msvc.api.users.entities.Role;
import com.digital.money.msvc.api.users.entities.User;
import com.digital.money.msvc.api.users.exceptions.HasAlreadyBeenRegistred;
import com.digital.money.msvc.api.users.exceptions.UserNotFoundException;
import com.digital.money.msvc.api.users.mappers.UserMapper;
import com.digital.money.msvc.api.users.repositorys.IRoleRepository;
import com.digital.money.msvc.api.users.repositorys.IUserRepository;
import com.digital.money.msvc.api.users.services.IUserService;
import com.digital.money.msvc.api.users.utils.KeysGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bcrypt;

    public UserService(IUserRepository userRepository, IRoleRepository roleRepository, UserMapper userMapper, BCryptPasswordEncoder bcrypt) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.bcrypt = bcrypt;
    }

    @Transactional
    @Override
    public UserDTO createUser(UserRequestDTO userRequestDTO) throws Exception {

        Optional<User> userEntityResponse = userRepository.findByDni(userRequestDTO.getDni());

        if (userEntityResponse.isPresent()) {
            throw new HasAlreadyBeenRegistred(String
                    .format("The user %s is already registered in the database", userRequestDTO.getName()));
        }

        Role role = roleRepository.findById(userRequestDTO.getRole().getRoleId()).orElseThrow(() ->
                new Exception(String.format("The role with Id %d does not exist", userRequestDTO.getRole().getRoleId())));

        User userEntity = userMapper.mapToEntity(userRequestDTO);
        userEntity.setEmail(userRequestDTO.getEmail().toLowerCase());
        userEntity.setCvu(KeysGenerator.generateCvu());
        userEntity.setAlias(KeysGenerator.generateAlias());
        userEntity.setEnabled(true);
        userEntity.setAttempts(0);
        userEntity.setRole(role);
        userEntity.setPassword(bcrypt.encode(userEntity.getPassword()));

        return userMapper.mapToDto(userRepository.save(userEntity));
    }

    @Transactional(readOnly = true)
    @Override
    public UserDTO getUserByEmail(String email) throws UserNotFoundException {
        String emailInLowercase = email.toLowerCase();

        User user = userRepository.findByEmail(emailInLowercase).orElseThrow(
                () -> new UserNotFoundException(String
                        .format("The user with email %s was not found", email))
        );

        return userMapper.mapToDto(user);
    }
}
