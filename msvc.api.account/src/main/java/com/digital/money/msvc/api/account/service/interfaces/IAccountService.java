package com.digital.money.msvc.api.account.service.interfaces;

import com.digital.money.msvc.api.account.handler.AlreadyRegisteredException;
import com.digital.money.msvc.api.account.handler.BadRequestException;
import com.digital.money.msvc.api.account.handler.ForbiddenException;
import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.Transaction;
import com.digital.money.msvc.api.account.model.dto.*;
import com.digital.money.msvc.api.account.service.ICheckId;
import org.json.JSONException;

import java.util.List;

public interface IAccountService extends ICheckId<Account> {
    //* ///////// ACCOUNT ///////// *//
    AccountGetDto save(Long id);
    AccountGetDto findById(Long id, String token) throws ResourceNotFoundException, ForbiddenException, JSONException;
    String updateAlias(Long id, AliasUpdate aliasUpdate, String token) throws AlreadyRegisteredException, ResourceNotFoundException, BadRequestException, ForbiddenException, JSONException;

    //* ///////// TRANSACTIONS ///////// *//
    ListTransactionDto findLastFiveTransactions(Long id, String token) throws ResourceNotFoundException, ForbiddenException, JSONException;
    ListTransactionDto findAllTransactions(Long id, String token) throws ResourceNotFoundException, ForbiddenException, JSONException;
    Transaction findTransactionById(Long accountId, Long transactionId, String token) throws Exception;

    //* ///////// CARDS ///////// *//
    CardGetDTO addCard(Long id, CardPostDTO cardPostDTO, String token) throws ResourceNotFoundException, AlreadyRegisteredException, BadRequestException, ForbiddenException, JSONException;
    List<CardGetDTO> listAllCards(Long id, String token) throws ResourceNotFoundException, ForbiddenException, JSONException;
    CardGetDTO findCardFromAccount(Long id, Long cardId, String token) throws ResourceNotFoundException, ForbiddenException, JSONException;
    void removeCardFromAccount(Long id, Long cardId, String token) throws ResourceNotFoundException, ForbiddenException, JSONException;
}
