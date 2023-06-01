package com.digital.money.msvc.api.account.service.impl;

import com.digital.money.msvc.api.account.handler.BadRequestException;
import com.digital.money.msvc.api.account.handler.CardAlreadyExistsException;
import com.digital.money.msvc.api.account.handler.CardNotFoundException;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.Card;
import com.digital.money.msvc.api.account.model.dto.CardGetDTO;
import com.digital.money.msvc.api.account.model.dto.CardPostDTO;
import com.digital.money.msvc.api.account.repository.ICardRepository;
import com.digital.money.msvc.api.account.service.interfaces.ICardService;
import com.digital.money.msvc.api.account.utils.mapper.CardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService implements ICardService{

    private final ICardRepository cardRepository;
    private final CardMapper cardMapper;

    @Autowired
    public CardService(ICardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    public CardGetDTO createCard(Account account, CardPostDTO cardPostDTO) throws CardAlreadyExistsException, BadRequestException {
        if (checkIfCardExists(cardPostDTO.getCardNumber())) {
            throw new CardAlreadyExistsException("The card you are trying to create already exists");
        }
        if (!isExpirationDateValid(cardPostDTO.getExpirationDate())) {
            throw new BadRequestException("The card you are trying to add is expired. " +
                    "Please make sure the expiration date is in the future.");
        }

        Card card = cardMapper.toCard(cardPostDTO);
        card.setAccount(account);
        cardRepository.save(card);

        return cardMapper.toCardGetDTO(card);
    }

    @Override
    public List<CardGetDTO> listCards(Account account) {
        List<Card> cards = cardRepository.findAllByAccountAccountId(account.getAccountId());
        return cards.stream().map(cardMapper::toCardGetDTO).collect(Collectors.toList());
    }

    @Override
    public CardGetDTO findCardById(Account account, Long cardId) throws CardNotFoundException {
        Optional<Card> entityResponse = cardRepository.findByCardId(cardId);

        if (entityResponse.isPresent()) {
            Card card = entityResponse.get();
            return cardMapper.toCardGetDTO(card);
        } else {
            throw new CardNotFoundException("The card you are trying to find does not exist");
        }
    }

    @Override
    public void deleteCard(Account account, Long cardId) throws CardNotFoundException {
        Optional<Card> entityResponse = cardRepository.findByCardId(cardId);

        if (entityResponse.isPresent()) {
            Card card = entityResponse.get();
            cardRepository.delete(card);
        } else {
            throw new CardNotFoundException("The card you are trying to delete does not exist");
        }
    }

    @Override
    public CardGetDTO save(CardPostDTO cardPostDTO) {
        return null;
    }

    private boolean checkIfCardExists(Long cardNumber) {
        return cardRepository.findByCardNumber(cardNumber).isPresent();
    }

    private boolean isExpirationDateValid(String expirationDate) {
        try {
            YearMonth yearMonth = YearMonth.parse(expirationDate, cardMapper.formatter);
            YearMonth currentYearMonth = YearMonth.now();
            return yearMonth.isAfter(currentYearMonth) || yearMonth.equals(currentYearMonth);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
