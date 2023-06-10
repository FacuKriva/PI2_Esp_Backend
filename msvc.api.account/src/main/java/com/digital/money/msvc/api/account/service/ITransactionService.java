package com.digital.money.msvc.api.account.service;

import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.handler.BadRequestException;
import com.digital.money.msvc.api.account.handler.PaymentRequiredException;
import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
import com.digital.money.msvc.api.account.handler.ForbiddenException;
import com.digital.money.msvc.api.account.model.Transaction;
import com.digital.money.msvc.api.account.model.dto.ListTransactionDto;
import com.digital.money.msvc.api.account.model.dto.CardTransactionGetDTO;
import com.digital.money.msvc.api.account.model.dto.CardTransactionPostDTO;
import com.digital.money.msvc.api.account.model.dto.TransactionGetDto;
import com.digital.money.msvc.api.account.model.dto.TransactionPostDto;
import com.digital.money.msvc.api.account.service.ICheckId;
import com.digital.money.msvc.api.account.service.IService;

public interface ITransactionService extends IService<TransactionPostDto, TransactionGetDto>, ICheckId<Transaction> {
    ListTransactionDto getLastFive(Long id, Account account);
    Transaction findTransactionById(Long accountId, Long transactionId) throws ResourceNotFoundException;
    ListTransactionDto findAllSorted(Long id, Account account);
    CardTransactionGetDTO processCardTransaction(Long accountId, CardTransactionPostDTO cardTransactionPostDTO) throws ResourceNotFoundException, ForbiddenException, PaymentRequiredException, BadRequestException;
}
