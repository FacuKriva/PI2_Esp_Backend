package com.digital.money.msvc.api.account.service.impl;

import com.digital.money.msvc.api.account.handler.CardAlreadyExistsException;
import com.digital.money.msvc.api.account.handler.CardNotFoundException;
import com.digital.money.msvc.api.account.handler.NoCardsException;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.Card;
import com.digital.money.msvc.api.account.model.dto.CardGetDTO;
import com.digital.money.msvc.api.account.model.dto.CardPostDTO;
import com.digital.money.msvc.api.account.repository.IAccountRepository;
import com.digital.money.msvc.api.account.repository.ICardRepository;
import com.digital.money.msvc.api.account.service.interfaces.ICardService;
import com.digital.money.msvc.api.account.utils.mapper.CardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService implements ICardService{

    @Autowired
    private final ICardRepository cardRepository;

    @Autowired
    private final IAccountRepository accountRepository;

    @Autowired
    private final CardMapper cardMapper;

    public CardService(ICardRepository cardRepository, IAccountRepository accountRepository,
                       CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    public CardGetDTO createCard(CardPostDTO cardPostDTO, Long id) throws CardAlreadyExistsException{
        Optional<Account> entityResponse = accountRepository.findById(id);
        Account account = entityResponse.get();

         if(checkIfCardExists(cardPostDTO.getCardNumber())) {
             throw new CardAlreadyExistsException("The card you want to add already exists in our database");
         } else {
             Card card = cardMapper.toCard(cardPostDTO);
             card.setAccount(account);
             card.setAlias(cardPostDTO.getAlias());
             card.setCardNumber(cardPostDTO.getCardNumber());
             card.setCardHolder(cardPostDTO.getCardHolder());
             card.setExpirationDate(cardPostDTO.getExpirationDate());
             card.setCvv(cardPostDTO.getCvv());
             card.setBank(cardPostDTO.getBank());
             card.setCardType(cardPostDTO.getCardType());
             Card cardSaved = cardRepository.save(card);

             return cardMapper.toCardGetDTO(cardSaved);
         }
    }

    @Override
    public List<Card> getAllCardsFromAccount(Long id) throws NoCardsException {
        Optional<Account> entityResponse = accountRepository.findById(id);
        Account account = entityResponse.get();

        if(account.getCards().isEmpty()) {
            throw new NoCardsException("You haven't added any cards yet");
        } else {
            return account.getCards();
        }
    }

    @Override
    public CardGetDTO getCardById(Long cardId) throws CardNotFoundException {
        Optional<Card> entityResponse = cardRepository.findById(cardId);

        if (entityResponse.isPresent()) {
        Card card = entityResponse.get();
        return cardMapper.toCardGetDTO(card);
        } else {
            throw new CardNotFoundException("We were unable to find the card you are looking for :(");
        }
    }

    @Override
    public void deleteCard(Long cardId) throws CardNotFoundException {
        Optional<Card> entityResponse = cardRepository.findById(cardId);

        if (entityResponse.isPresent()) {
            Card card = entityResponse.get();
            cardRepository.delete(card);
        } else {
            throw new CardNotFoundException("The card you are trying to delete does not exist");
        }
    }

    @Override
    public boolean checkIfCardExists(Long cardNumber) {
        return cardRepository.findByCardNumber(cardNumber).isPresent();
    }

    @Override
    public CardGetDTO save(CardPostDTO cardPostDTO) {
        return null;
    }
}
