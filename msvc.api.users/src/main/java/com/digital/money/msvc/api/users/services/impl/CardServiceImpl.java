package com.digital.money.msvc.api.users.services.impl;

import com.digital.money.msvc.api.users.controllers.requestDto.CardRequestDTO;
import com.digital.money.msvc.api.users.dtos.CardDTO;
import com.digital.money.msvc.api.users.entities.Card;
import com.digital.money.msvc.api.users.entities.User;
import com.digital.money.msvc.api.users.exceptions.BadRequestException;
import com.digital.money.msvc.api.users.exceptions.CardNotFoundException;
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

    public CardServiceImpl(ICardRepository cardRepository, IUserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CardDTO createCard(CardRequestDTO cardRequestDTO, Long dni) throws BadRequestException {

        Optional<User> entityResponse = userRepository.findByDni(dni);

        if (entityResponse.isPresent()) {
            User user = entityResponse.get();
            Card card = new Card();
            card.setUser(user);
            card.setAlias(cardRequestDTO.getAlias());
            card.setBank(cardRequestDTO.getBank());
            card.setCardNumber(cardRequestDTO.getCardNumber());
            card.setCardHolder(cardRequestDTO.getCardHolder());
            card.setExpirationDate(cardRequestDTO.getExpirationDate());
            card.setCvv(cardRequestDTO.getCvv());
            cardRepository.save(card);

            return new CardDTO();
        } else {
            throw new BadRequestException("Card could not be created");
        }
    }

    @Override
    public List<Card> getAllCardsFromUser(Long dni) {
        Optional<User> entityResponse = userRepository.findByDni(dni);
        User user = entityResponse.get();
        List<Card> cards = cardRepository.findByUser(user);
        return cards;
    }

    @Override
    public CardDTO getCardById(Long cardId) throws CardNotFoundException {

            Optional<Card> entityResponse = cardRepository.findById(cardId);

            if (entityResponse.isPresent()) {
                Card card = entityResponse.get();
                return new CardDTO();
            } else {
                throw new CardNotFoundException("Card not found");
            }
    }

    @Override
    public void deleteCard(Long cardId) throws CardNotFoundException {

            Optional<Card> entityResponse = cardRepository.findById(cardId);

            if (entityResponse.isPresent()) {
                cardRepository.deleteById(cardId);
            } else {
                throw new CardNotFoundException("Card not found");
            }
    }
}