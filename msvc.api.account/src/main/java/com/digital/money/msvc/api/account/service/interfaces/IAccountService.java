package com.digital.money.msvc.api.account.service.interfaces;

import com.digital.money.msvc.api.account.handler.AlreadyRegisteredException;
import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.dto.AccountGetDto;
import com.digital.money.msvc.api.account.model.dto.AccountPostDto;
import com.digital.money.msvc.api.account.model.dto.AliasUpdate;
import com.digital.money.msvc.api.account.model.dto.TransactionGetDto;
import com.digital.money.msvc.api.account.service.ICheckId;
import com.digital.money.msvc.api.account.service.IService;

import java.util.Set;

public interface IAccountService extends ICheckId<Account> {

    AccountGetDto save(Long id);
    AccountGetDto findById(Long id) throws ResourceNotFoundException;
    Set<TransactionGetDto> findAllByAccountId(Long id) throws ResourceNotFoundException;
    void updateAlias(Long id, AliasUpdate aliasUpdate) throws AlreadyRegisteredException, ResourceNotFoundException;

}
