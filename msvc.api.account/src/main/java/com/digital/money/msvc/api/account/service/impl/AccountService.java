package com.digital.money.msvc.api.account.service.impl;

import com.digital.money.msvc.api.account.handler.AlreadyRegisteredException;
import com.digital.money.msvc.api.account.handler.BadRequestException;
import com.digital.money.msvc.api.account.handler.NoTransactionsException;
import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.dto.AccountGetDto;
import com.digital.money.msvc.api.account.model.dto.AliasUpdate;
import com.digital.money.msvc.api.account.model.dto.TransactionGetDto;
import com.digital.money.msvc.api.account.repository.IAccountRepository;
import com.digital.money.msvc.api.account.service.interfaces.IAccountService;
import com.digital.money.msvc.api.account.utils.KeysGenerator;
import com.digital.money.msvc.api.account.utils.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountService implements IAccountService {
    @Autowired
    protected AccountMapper accountMapper;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    protected KeysGenerator keysGenerator;
    @Autowired
    protected IAccountRepository accountRepository;

    @Override
    public AccountGetDto findById(Long id) throws ResourceNotFoundException {
        Account account = checkId(id);
        AccountGetDto accountGetDto = accountMapper.toAccountGetDto(account);
        return accountGetDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionGetDto> findAllByAccountId(Long id) throws ResourceNotFoundException, NoTransactionsException {
        return transactionService.getLastFive(id);
    }

    @Transactional
    @Override
    public AccountGetDto save(Long userId) {
        Account account = new Account();
        account.setUserId(userId);

        String cvu = keysGenerator.generateCvu();
        while (accountRepository.findByCvu(cvu).isPresent()) {
            cvu = keysGenerator.generateCvu();
        }
        account.setCvu(cvu);

        String alias = keysGenerator.generateAlias();
        while (accountRepository.findByCvu(alias).isPresent()) {
            alias = keysGenerator.generateCvu();
        }
        account.setAlias(alias);
        account.setAvailableBalance(0.0);

        Account accountResponse = accountRepository.save(account);
        return accountMapper.toAccountGetDto(accountResponse);
    }

    @Transactional
    @Override
    public String updateAlias(Long id, AliasUpdate aliasUpdate) throws AlreadyRegisteredException, ResourceNotFoundException, BadRequestException {
        Account account = checkId(id);
        String newAlias = aliasUpdate.buildAlias().toLowerCase();

        if (newAlias.equals(account.getAlias())) {
            throw new AlreadyRegisteredException("The alias is already registered");
        }

        Optional<Account> duplicateAlias = accountRepository.findByAlias(newAlias);
        if (!duplicateAlias.isPresent()) {
            account.setAlias(newAlias);
            accountRepository.save(account);
            return String.format("New Alias: %s", account.getAlias());
        } else {
            throw new AlreadyRegisteredException("The alias is already registered");
        }
    }

    @Override
    public Account checkId(Long id) throws ResourceNotFoundException {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException(msjIdError + " id: " + id);
        }
        return account.get();
    }

//    private void checkUnique(Account account) throws AlreadyRegisteredException {
//        String alias = account.getAlias();
//        if (accountRepository.aliasUnique(alias, account.getAccountId()).isPresent()) {
//            throw new AlreadyRegisteredException("The user with alias " + alias + " is already registred");
//        }
//    }

}
