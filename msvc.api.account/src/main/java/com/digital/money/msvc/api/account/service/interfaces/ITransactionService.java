package com.digital.money.msvc.api.account.service.interfaces;

import com.digital.money.msvc.api.account.handler.PaymentRequiredException;
import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
import com.digital.money.msvc.api.account.handler.UnauthorizedException;
import com.digital.money.msvc.api.account.model.Transaction;
import com.digital.money.msvc.api.account.model.dto.CardTransactionGetDTO;
import com.digital.money.msvc.api.account.model.dto.CardTransactionPostDTO;
import com.digital.money.msvc.api.account.model.dto.TransactionGetDto;
import com.digital.money.msvc.api.account.model.dto.TransactionPostDto;
import com.digital.money.msvc.api.account.service.ICheckId;
import com.digital.money.msvc.api.account.service.IService;

public interface ITransactionService extends IService<TransactionPostDto, TransactionGetDto>, ICheckId<Transaction> {
    CardTransactionGetDTO processCardTransaction(Long accountId, CardTransactionPostDTO cardTransactionPostDTO) throws ResourceNotFoundException, UnauthorizedException, PaymentRequiredException;
}
