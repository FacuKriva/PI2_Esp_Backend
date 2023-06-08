package com.digital.money.msvc.api.account.service.impl;

import com.digital.money.msvc.api.account.handler.*;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.dto.*;
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
    private final AccountMapper accountMapper;
    private final TransactionService transactionService;
    private final KeysGenerator keysGenerator;
    private final IAccountRepository accountRepository;
    private final CardService cardService;

    @Autowired
    public AccountService(AccountMapper accountMapper, TransactionService transactionService,
                          KeysGenerator keysGenerator, IAccountRepository accountRepository,
                          CardService cardService) {
        this.accountMapper = accountMapper;
        this.transactionService = transactionService;
        this.keysGenerator = keysGenerator;
        this.accountRepository = accountRepository;
        this.cardService = cardService;
    }

    @Override
    public AccountGetDto findById(Long id) throws ResourceNotFoundException {
        Account account = checkId(id);
        AccountGetDto accountGetDto = accountMapper.toAccountGetDto(account);
        return accountGetDto;
    }

    @Transactional(readOnly = true)
    @Override
    public LastFiveTransactionDto findAllByAccountId(Long id) throws ResourceNotFoundException  {
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
//            throw new AlreadyRegisteredException("The user with alias " + alias + " is already registered");
//        }
//    }

    @Transactional
    @Override
    public CardGetDTO addCard(Long id, CardPostDTO cardPostDTO) throws ResourceNotFoundException, AlreadyRegisteredException, BadRequestException {
        Account account = checkId(id);
        return cardService.createCard(account, cardPostDTO);
    }

    @Override
    public List<CardGetDTO> listAllCards(Long id) throws ResourceNotFoundException {
        Account account = checkId(id);
        return cardService.listCards(account);
    }

    @Override
    public CardGetDTO findCardFromAccount(Long id, Long cardId) throws ResourceNotFoundException {
        Account account = checkId(id);
        return cardService.findCardById(account, cardId);
    }

    @Override
    public void removeCardFromAccount(Long id, Long cardId) throws ResourceNotFoundException {
        Account account = checkId(id);
        cardService.deleteCard(account, cardId);
    }

    @Transactional
    @Override
    public CardTransactionGetDTO depositMoney(Long id, CardTransactionPostDTO cardTransactionPostDTO) throws ResourceNotFoundException, PaymentRequiredException, UnauthorizedException {
        return transactionService.processCardTransaction(id, cardTransactionPostDTO);
    }

}
