package com.digital.money.msvc.api.account.service.interfaces;

import com.digital.money.msvc.api.account.handler.CardAlreadyExistsException;
import com.digital.money.msvc.api.account.handler.CardNotFoundException;
import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.dto.CardGetDTO;
import com.digital.money.msvc.api.account.model.dto.CardPostDTO;
import com.digital.money.msvc.api.account.service.ICheckId;
import com.digital.money.msvc.api.account.service.IService;

import java.util.List;

public interface ICardService extends IService<CardPostDTO, CardGetDTO> {

    CardGetDTO createCard(Account account, CardPostDTO cardPostDTO) throws CardAlreadyExistsException;
    List<CardGetDTO> listCards(Account account) throws ResourceNotFoundException;
    CardGetDTO findCardById(Account account, Long cardId) throws CardNotFoundException, ResourceNotFoundException;
    void deleteCard(Account account, Long cardId) throws CardNotFoundException;
    boolean checkIfCardExists(Long cardNumber);

}
