package com.digital.money.msvc.api.account.service.interfaces;

import com.digital.money.msvc.api.account.handler.*;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.dto.CardGetDTO;
import com.digital.money.msvc.api.account.model.dto.CardPostDTO;
import com.digital.money.msvc.api.account.service.IService;

import java.util.List;

public interface ICardService extends IService<CardPostDTO, CardGetDTO> {

    CardGetDTO createCard(Account account, CardPostDTO cardPostDTO) throws AlreadyRegisteredException, BadRequestException;
    List<CardGetDTO> listCards(Account account) throws ResourceNotFoundException;
    CardGetDTO findCardById(Account account, Long cardId) throws ResourceNotFoundException, ForbiddenException;
    void deleteCard(Account account, Long cardId) throws ResourceNotFoundException;

}
