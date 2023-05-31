package com.digital.money.msvc.api.account.service.interfaces;

import com.digital.money.msvc.api.account.handler.CardAlreadyExistsException;
import com.digital.money.msvc.api.account.handler.CardNotFoundException;
import com.digital.money.msvc.api.account.handler.NoCardsException;
import com.digital.money.msvc.api.account.model.Card;
import com.digital.money.msvc.api.account.model.dto.CardGetDTO;
import com.digital.money.msvc.api.account.model.dto.CardPostDTO;
import com.digital.money.msvc.api.account.service.IService;

import java.util.List;

public interface ICardService extends IService<CardPostDTO, CardGetDTO> {

        CardGetDTO createCard(CardPostDTO cardPostDTO, Long id) throws CardAlreadyExistsException;
        List<Card> getAllCardsFromAccount(Long id) throws NoCardsException;
        CardGetDTO getCardById(Long cardId) throws CardNotFoundException;
        void deleteCard(Long cardId) throws CardNotFoundException;
        boolean checkIfCardExists(Long cardNumber);

}
