package com.digital.money.msvc.api.users.services.impl;

import com.digital.money.msvc.api.users.controllers.requestDto.CardRequestDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.NewPassDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.controllers.requestDto.VerficationRequestDTO;
import com.digital.money.msvc.api.users.dtos.AuthUserDTO;
import com.digital.money.msvc.api.users.dtos.CardDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;
import com.digital.money.msvc.api.users.entities.Card;
import com.digital.money.msvc.api.users.entities.Role;
import com.digital.money.msvc.api.users.entities.User;
import com.digital.money.msvc.api.users.entities.Verified;
import com.digital.money.msvc.api.users.exceptions.*;
import com.digital.money.msvc.api.users.mappers.UserMapper;
import com.digital.money.msvc.api.users.repositorys.ICardRepository;
import com.digital.money.msvc.api.users.repositorys.IRoleRepository;
import com.digital.money.msvc.api.users.repositorys.IUserRepository;
import com.digital.money.msvc.api.users.services.IUserService;
import com.digital.money.msvc.api.users.utils.KeysGenerator;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService implements IUserService {

    private static final int ROLE_USER = 2;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bcrypt;
    private final EmailServiceImpl emailService;
    private final VerificationServiceImpl verificationService;
    private final CardServiceImpl cardService;
    private final ICardRepository cardRepository;

    public UserService(IUserRepository userRepository, IRoleRepository roleRepository, UserMapper userMapper, BCryptPasswordEncoder bcrypt, EmailServiceImpl emailService, VerificationServiceImpl verificationService, EmailServiceImpl emailService1, VerificationServiceImpl verificationService1, CardServiceImpl cardService, ICardRepository cardRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.bcrypt = bcrypt;
        this.emailService = emailService1;
        this.verificationService = verificationService1;
        this.cardService = cardService;
        this.cardRepository = cardRepository;
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
        emailService.sendVericationMail(user,codigo);
    }

    @Override
    public ResponseEntity <String> verificateUser(VerficationRequestDTO verficationRequestDTO, String token) throws JSONException {

        String[] jwtParts = token.split("\\.");
        JSONObject payload = new JSONObject(decodeToken(jwtParts[1]));
        String email = payload.getString("email");

        User user = userRepository.findByEmail(email).get();

        Verified verified = new Verified(user.getUserId(), verficationRequestDTO.getVerificationCode());
        Boolean checkedCode = verificationService.verificateCode(verified);

        if(!checkedCode)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The code entered is incorrect");

        user.setVerified(true);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("Your email has been successfully verified");
    }

    public void resendVerificationMail(String token) throws Exception {
        String[] jwtParts = token.split("\\.");
        JSONObject payload = new JSONObject(decodeToken(jwtParts[1]));
        String email = payload.getString("email");
        Optional<User> user = userRepository.findByEmail(email);
        if (!(user.get().getVerified())) {
                sendVerificationMail(email);
            } else throw new BadRequestException("The user email is already verified");
    }

    private static String decodeToken(String token) {
        return new String(Base64.getUrlDecoder().decode(token));
    }

    @Override
    public void forgotPassword(String email) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String link = verificationService.createRecoverPasswordLink(user.get().getUserId());
            emailService.sendForgotPasswordEmail(user.get(),link);
        } else throw new UserNotFoundException("Please provide a valid email address in order to recover your password");
    }

    @Override
    public void resetPassword(String recoveryLink, NewPassDTO passDTO) throws PasswordNotChangedException {

        if(!passDTO.getPass().equals(passDTO.getPassRep()))
            throw new PasswordNotChangedException("The passwords don't match");

        String newPassword = passDTO.getPass();

        Boolean codigoVerificado = verificationService.verificateRecoveryLink(recoveryLink);

        if (!codigoVerificado)
            throw new PasswordNotChangedException("The link does not exist");

        String strUserId = recoveryLink.substring(0, recoveryLink.length()-6);
        Long userId = Long.parseLong(strUserId);

        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        if (bcrypt.matches(newPassword, user.getPassword())) {
            throw new PasswordNotChangedException ("The new password must be different than the previous one");
        }
        user.setPassword(bcrypt.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void addCardToAccount(Long dni, CardRequestDTO cardRequestDTO) throws CardAlreadyExistsException {
        cardService.createCard(cardRequestDTO, dni);
    }

    @Override
    public void removeCardFromAccount(Long dni, Long cardId) throws UserNotFoundException, CardNotFoundException {
        Optional<User> user = userRepository.findByDni(dni);
        if (user.isPresent()) {
            Optional<Card> card = cardRepository.findById(cardId);
            if (card.isPresent()) {
                cardService.deleteCard(cardId);
            } else throw new CardNotFoundException("The card does not exist");
        } else throw new UserNotFoundException("User not found");
    }

    @Override
    public List<Card> getAllCardsFromAccount(Long dni) throws UserNotFoundException, NoCardsException {
        Optional<User> user = userRepository.findByDni(dni);
        if (user.isPresent()) {
            List<Card> cards = cardService.getAllCardsFromUser(user.get().getDni());
            if (cards.isEmpty()) {
                throw new NoCardsException("The user has no cards");
            } else return cards;
        } else throw new UserNotFoundException("User not found");
    }

    @Override
    public CardDTO getCardFromAccount(Long dni, Long cardId) throws UserNotFoundException, CardNotFoundException {
        Optional<User> user = userRepository.findByDni(dni);
        if (user.isPresent()) {
            Optional<Card> card = cardRepository.findById(cardId);
            if (card.isPresent()) {
                return cardService.getCardById(cardId);
            } else throw new CardNotFoundException("Card does not exist");
        } else throw new UserNotFoundException("The user is not registered");
    }

    @Override
    public boolean cardAlreadyExists(Long cardNumber) throws CardAlreadyExistsException {
        if (cardRepository.findByCardNumber(cardNumber).isPresent())
            throw new CardAlreadyExistsException("The card is already registered in the system");
        return false;
    }

}