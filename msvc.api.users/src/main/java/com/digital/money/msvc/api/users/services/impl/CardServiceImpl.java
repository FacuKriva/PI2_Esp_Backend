package com.digital.money.msvc.api.users.services.impl;

import com.digital.money.msvc.api.users.controllers.requestDto.CardRequestDTO;
import com.digital.money.msvc.api.users.dtos.CardDTO;
import com.digital.money.msvc.api.users.entities.Card;
import com.digital.money.msvc.api.users.entities.User;
import com.digital.money.msvc.api.users.exceptions.CardAlreadyExistsException;
import com.digital.money.msvc.api.users.exceptions.CardNotFoundException;
import com.digital.money.msvc.api.users.mappers.CardMapper;
import com.digital.money.msvc.api.users.repositorys.ICardRepository;
import com.digital.money.msvc.api.users.repositorys.IUserRepository;
import com.digital.money.msvc.api.users.services.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements ICardService {

    @Autowired
    private final ICardRepository cardRepository;

    @Autowired
    private final IUserRepository userRepository;

    @Autowired
    private final CardMapper cardMapper;

    public CardServiceImpl(ICardRepository cardRepository, IUserRepository userRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    public CardDTO createCard(CardRequestDTO cardRequestDTO, Long dni) throws CardAlreadyExistsException {
        Optional<User> entityResponse = userRepository.findByDni(dni);
        User user = entityResponse.get();

        if (checkCardExists(cardRequestDTO.getCardNumber())) {
            throw new CardAlreadyExistsException("Card already exists");
        } else {
            Card card = cardMapper.mapRequestToEntity(cardRequestDTO);
            card.setUser(user);
            card.setAlias(cardRequestDTO.getAlias());
            card.setCardNumber(cardRequestDTO.getCardNumber());
            card.setCardHolder(cardRequestDTO.getCardHolder());
            card.setExpirationDate(cardRequestDTO.getExpirationDate());
            card.setCvv(cardRequestDTO.getCvv());
            card.setBank(cardRequestDTO.getBank());
            card.setCardType(cardRequestDTO.getCardType());
            Card cardSaved = cardRepository.save(card);

            return cardMapper.mapToDto(cardSaved);
        }
    }

    @Override
    public List<Card> getAllCardsFromUser(Long dni) {
        Optional<User> entityResponse = userRepository.findByDni(dni);
        User user = entityResponse.get();
        return cardRepository.findByUser(user);
    }

    @Override
    public CardDTO getCardById(Long cardId) throws CardNotFoundException {
        Optional<Card> entityResponse = cardRepository.findById(cardId);

        if (entityResponse.isPresent()) {
            Card card = entityResponse.get();
            return cardMapper.mapToDto(card);
        } else {
            throw new CardNotFoundException("Card not found");
        }
    }

    @Override
    public void deleteCard(Long cardId) throws CardNotFoundException {
        Optional<Card> entityResponse = cardRepository.findById(cardId);

        if (entityResponse.isPresent()) {
            Card card = entityResponse.get();
            cardRepository.delete(card);
        } else {
            throw new CardNotFoundException("Card not found");
        }
    }
}