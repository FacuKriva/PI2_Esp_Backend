package com.digital.money.msvc.api.users.services.impl;

import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.dtos.AuthUserDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;
import com.digital.money.msvc.api.users.entities.Role;
import com.digital.money.msvc.api.users.entities.User;
import com.digital.money.msvc.api.users.exceptions.HasAlreadyBeenRegistred;
import com.digital.money.msvc.api.users.exceptions.InternalServerError;
import com.digital.money.msvc.api.users.exceptions.UserNotFoundException;
import com.digital.money.msvc.api.users.mappers.UserMapper;
import com.digital.money.msvc.api.users.repositorys.IRoleRepository;
import com.digital.money.msvc.api.users.repositorys.IUserRepository;
import com.digital.money.msvc.api.users.services.IUserService;
import com.digital.money.msvc.api.users.utils.KeysGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private static final int ROLE_USER = 2;
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
                    .format("The user %s is already registered", userRequestDTO.getName()));
        }

        Role role = roleRepository.findById(ROLE_USER).get();

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
    public UserDTO getUserByDni(Long dni) throws UserNotFoundException {

        User user = userRepository.findByDni(dni).orElseThrow(
                () -> new UserNotFoundException(String
                        .format("The user with dni %d was not found", dni))
        );

        return userMapper.mapToDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public AuthUserDTO getUserByEmail(String email) throws UserNotFoundException {
        String emailInLowercase = email.toLowerCase();

        User user = userRepository.findByEmail(emailInLowercase).orElseThrow(
                () -> new UserNotFoundException(String
                        .format("The user with email %s was not found", email))
        );

        return userMapper.mapToAuthUserDto(user);
    }

    @Transactional
    @Override
    public void updateAttempsFromUser(Long userId, boolean enabled, int attempts) throws UserNotFoundException {
        Optional<User> userEntity = userRepository.findByDni(userId);

        if (userEntity.isEmpty()) {
            throw new UserNotFoundException("The user is not registered");
        }

        User user = userEntity.get();
        user.setEnabled(enabled);
        user.setAttempts(attempts);

        userRepository.save(user);
    }
}
