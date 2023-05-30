package com.digital.money.msvc.api.account.service.interfaces;

import com.digital.money.msvc.api.account.handler.AlreadyRegisteredException;
import com.digital.money.msvc.api.account.handler.BadRequestException;
import com.digital.money.msvc.api.account.handler.NoTransactionsException;
import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.dto.AccountGetDto;
import com.digital.money.msvc.api.account.model.dto.AliasUpdate;
import com.digital.money.msvc.api.account.model.dto.TransactionGetDto;
import com.digital.money.msvc.api.account.service.ICheckId;

import java.util.List;

public interface IAccountService extends ICheckId<Account> {

    AccountGetDto save(Long id);
    AccountGetDto findById(Long id) throws ResourceNotFoundException;
    List<TransactionGetDto> findAllByAccountId(Long id) throws ResourceNotFoundException, NoTransactionsException;
    String updateAlias(Long id, AliasUpdate aliasUpdate) throws AlreadyRegisteredException, ResourceNotFoundException, BadRequestException;

}
