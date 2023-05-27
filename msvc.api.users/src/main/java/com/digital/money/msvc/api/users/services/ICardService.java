package com.digital.money.msvc.api.users.services;

import com.digital.money.msvc.api.users.controllers.requestDto.CardRequestDTO;
import com.digital.money.msvc.api.users.dtos.CardDTO;
import com.digital.money.msvc.api.users.entities.Card;
import com.digital.money.msvc.api.users.exceptions.BadRequestException;
import com.digital.money.msvc.api.users.exceptions.CardNotFoundException;

import java.util.List;

public interface ICardService {

    CardDTO createCard(CardRequestDTO cardRequestDTO, Long userId) throws BadRequestException;
    List<Card> getAllCardsFromUser(Long dni);
    CardDTO getCardById(Long cardId) throws CardNotFoundException;
    void deleteCard(Long cardId) throws CardNotFoundException;
}
