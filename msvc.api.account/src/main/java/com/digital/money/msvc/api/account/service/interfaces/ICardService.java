package com.digital.money.msvc.api.account.service.interfaces;

import com.digital.money.msvc.api.account.handler.CardAlreadyExistsException;
import com.digital.money.msvc.api.account.handler.CardNotFoundException;
import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
import com.digital.money.msvc.api.account.model.dto.CardGetDTO;
import com.digital.money.msvc.api.account.model.dto.CardPostDTO;
import com.digital.money.msvc.api.account.service.IService;

import java.util.List;

public interface ICardService extends IService<CardPostDTO, CardGetDTO> {

    CardGetDTO addCardToAccount(Long id, CardPostDTO cardPostDTO) throws CardAlreadyExistsException, ResourceNotFoundException;
    List<CardGetDTO> listCardsFromAccount(Long id) throws ResourceNotFoundException;
    CardGetDTO findCardFromAccount(Long id, Long cardNumber) throws CardNotFoundException, ResourceNotFoundException;
    void removeCardFromAccount(Long id, Long cardId) throws CardNotFoundException, ResourceNotFoundException;
    boolean checkIfCardExists(Long cardNumber);

}
