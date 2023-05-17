package com.digital.money.msvc.api.users.services.impl;

import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.VerficationRequestDTO;
import com.digital.money.msvc.api.users.dtos.AuthUserDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;
import com.digital.money.msvc.api.users.entities.Role;
import com.digital.money.msvc.api.users.entities.User;
import com.digital.money.msvc.api.users.entities.Verified;
import com.digital.money.msvc.api.users.exceptions.HasAlreadyBeenRegistred;
import com.digital.money.msvc.api.users.exceptions.PasswordNotChangedException;
import com.digital.money.msvc.api.users.exceptions.UserNotFoundException;
import com.digital.money.msvc.api.users.mappers.UserMapper;
import com.digital.money.msvc.api.users.repositorys.IRoleRepository;
import com.digital.money.msvc.api.users.repositorys.IUserRepository;
import com.digital.money.msvc.api.users.services.IUserService;
import com.digital.money.msvc.api.users.utils.KeysGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private static final int ROLE_USER = 2;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bcrypt;
    private final EmailServiceImpl emailService;
    private final VerificationServiceImpl verificationService;

    public UserService(IUserRepository userRepository, IRoleRepository roleRepository, UserMapper userMapper, BCryptPasswordEncoder bcrypt, EmailServiceImpl emailService, VerificationServiceImpl verificationService, EmailServiceImpl emailService1, VerificationServiceImpl verificationService1) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.bcrypt = bcrypt;
        this.emailService = emailService1;
        this.verificationService = verificationService1;
    }

    @Transactional
    @Override
    public UserDTO createUser(UserRequestDTO userRequestDTO) throws Exception {

        Optional<User> entityResponseDni = userRepository.findByDni(userRequestDTO.getDni());
        Optional<User> entityResponseEmail = userRepository.findByEmail(userRequestDTO.getEmail());

        if (entityResponseDni.isPresent() || entityResponseEmail.isPresent()) {
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
        userEntity.setVerified(false);
        User userSaved = userRepository.save(userEntity);

        System.out.println(userSaved);

        sendVerificationMail(userEntity.getEmail());

        return userMapper.mapToDto(userSaved);
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

    @Override
    public void sendVerificationMail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        Integer codigo = verificationService.createVerificationCode(user.getUserId());
        emailService.sendMail(user,codigo);
    }

    @Override
    public String verificateUser(VerficationRequestDTO verficationRequestDTO) {

        String email = verficationRequestDTO.getMail();
        User user = userRepository.findByEmail(email).get();

        Verified verified = new Verified(user.getUserId(), verficationRequestDTO.getVerificationCode());
        Boolean checkedCode = verificationService.verificateCode(verified);

        if(!checkedCode)
            return "Código erroneo";

        user.setVerified(true);

        userRepository.save(user);

        return "Cuenta verificada";
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        String link = verificationService.createRecoverPasswordLink(user.getUserId());
        emailService.sendForgotPasswordEmail(user,link);
    }

    @Override
    public void resetPassword(String email, String newPassword) throws PasswordNotChangedException {
        User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        if (bcrypt.matches(newPassword, user.getPassword())) {
            throw new PasswordNotChangedException("La contraseña no puede ser la misma que la anterior");
        }
        user.setPassword(bcrypt.encode(newPassword));
        userRepository.save(user);
    }
}
