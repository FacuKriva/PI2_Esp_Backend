package com.digital.money.msvc.api.account.service.interfaces;

import com.digital.money.msvc.api.account.handler.AlreadyRegisteredException;
import com.digital.money.msvc.api.account.handler.BadRequestException;
import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.Transaction;
import com.digital.money.msvc.api.account.model.dto.*;
import com.digital.money.msvc.api.account.service.ICheckId;

import java.util.List;

public interface IAccountService extends ICheckId<Account> {

    AccountGetDto save(Long id);
    AccountGetDto findById(Long id) throws ResourceNotFoundException;
    LastFiveTransactionDto findAllByAccountId(Long id) throws ResourceNotFoundException;
    String updateAlias(Long id, AliasUpdate aliasUpdate) throws AlreadyRegisteredException, ResourceNotFoundException, BadRequestException;

    CardGetDTO addCard(Long id, CardPostDTO cardPostDTO) throws ResourceNotFoundException, AlreadyRegisteredException, BadRequestException;
    List<CardGetDTO> listAllCards(Long id) throws ResourceNotFoundException;
    CardGetDTO findCardFromAccount(Long id, Long cardId) throws ResourceNotFoundException;
    void removeCardFromAccount(Long id, Long cardId) throws ResourceNotFoundException;

    Transaction findTransactionById(Long accountId, Long transactionId, String token) throws Exception;
}
