package com.digital.money.msvc.api.account.service.impl;

import com.digital.money.msvc.api.account.handler.CardAlreadyExistsException;
import com.digital.money.msvc.api.account.handler.CardNotFoundException;
import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
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
import java.util.stream.Collectors;

@Service
public class CardService implements ICardService{

    private final ICardRepository cardRepository;
    private final IAccountRepository accountRepository;
    private final CardMapper cardMapper;
    private final AccountService accountService;

    @Autowired
    public CardService(ICardRepository cardRepository, IAccountRepository accountRepository,
                       CardMapper cardMapper, AccountService accountService) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.cardMapper = cardMapper;
        this.accountService = accountService;
    }

    @Override
    public CardGetDTO addCardToAccount(Long id, CardPostDTO cardPostDTO) throws CardAlreadyExistsException, ResourceNotFoundException {
        Account account = accountService.checkId(id);

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
    public List<CardGetDTO> listCardsFromAccount(Long id) throws ResourceNotFoundException {
        Account account = accountService.checkId(id);

        Optional<List<Card>> listCards = cardRepository.findAllByAccountId(account.getAccountId());
        return listCards.get().stream()
                .map(card -> cardMapper.toCardGetDTO(card))
                .collect(Collectors.toList());
    }

    @Override
    public CardGetDTO findCardFromAccount(Long id, Long cardId) throws CardNotFoundException, ResourceNotFoundException {
        Account account = accountService.checkId(id);

        Optional<Card> entityResponse = cardRepository.findByCardById(cardId);

        if (entityResponse.isPresent()) {
        Card card = entityResponse.get();
        return cardMapper.toCardGetDTO(card);
        } else {
            throw new CardNotFoundException("We were unable to find the card you are looking for :(");
        }
    }

    @Override
    public void removeCardFromAccount(Long id, Long cardId) throws CardNotFoundException, ResourceNotFoundException {
        Account account = accountService.checkId(id);

        Optional<Card> entityResponse = cardRepository.findByCardById(cardId);

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
