package com.digital.money.msvc.api.account.service.impl;

import com.digital.money.msvc.api.account.handler.*;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.Transaction;
import com.digital.money.msvc.api.account.model.dto.*;
import com.digital.money.msvc.api.account.repository.IAccountRepository;
import com.digital.money.msvc.api.account.service.IAccountService;
import com.digital.money.msvc.api.account.utils.GeneratorKeys;
import com.digital.money.msvc.api.account.utils.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountService implements IAccountService {
    private final AccountMapper accountMapper;
    private final TransactionService transactionService;
    private final IAccountRepository accountRepository;
    private final CardServices cardService;

    @Autowired
    public AccountService(AccountMapper accountMapper,
                          TransactionService transactionService,
                          IAccountRepository accountRepository,
                          CardServices cardService) {
        this.accountMapper = accountMapper;
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
        this.cardService = cardService;
    }

    //* ///////// ACCOUNT ///////// *//
    @Override
    public AccountGetDto findById(Long id, String token) throws ResourceNotFoundException, ForbiddenException, JSONException {
        Account account = checkId(id);
        validateAccountBelongsUser(account, token);
        return accountMapper.toAccountGetDto(account);
    }

    @Transactional
    @Override
    public AccountGetDto save(Long userId) {
        Account account = new Account();
        account.setUserId(userId);

        String cvu = GeneratorKeys.generateCvu();
        while (accountRepository.findByCvu(cvu).isPresent()) {
            cvu = GeneratorKeys.generateCvu();
        }
        account.setCvu(cvu);

        String alias = GeneratorKeys.generateAlias();
        while (accountRepository.findByCvu(alias).isPresent()) {
            alias = GeneratorKeys.generateCvu();
        }
        account.setAlias(alias);
        account.setAvailableBalance(0.0);

        Account accountResponse = accountRepository.save(account);
        return accountMapper.toAccountGetDto(accountResponse);
    }

    @Transactional
    @Override
    public String updateAlias(Long id, AliasUpdate aliasUpdate, String token) throws AlreadyRegisteredException, ResourceNotFoundException, BadRequestException, ForbiddenException, JSONException {
        Account account = checkId(id);
        validateAccountBelongsUser(account, token);
        String newAlias = aliasUpdate.buildAlias().toLowerCase();

        if (newAlias.equals(account.getAlias())) {
            throw new AlreadyRegisteredException("The alias is already registered");
        }

        Optional<Account> duplicateAlias = accountRepository.findByAlias(newAlias);
        if (duplicateAlias.isEmpty()) {
            account.setAlias(newAlias);
            accountRepository.save(account);
            return String.format("New Alias: %s", account.getAlias());
        } else {
            throw new AlreadyRegisteredException("The alias is already registered");
        }
    }

    //* ///////// TRANSACTIONS ///////// *//
    @Transactional(readOnly = true)
    @Override
    public ListTransactionDto findLastFiveTransactions(Long id, String token) throws ResourceNotFoundException, ForbiddenException, JSONException {
        Account account = checkId(id);
        validateAccountBelongsUser(account, token);
        return transactionService.getLastFive(id, account);
    }

    @Override
    public ListTransactionDto findAllTransactions(Long id, String token) throws ResourceNotFoundException, ForbiddenException, JSONException {
        Account account = checkId(id);
        validateAccountBelongsUser(account, token);

        return transactionService.findAllSorted(id, account);
    }

    @Override
    public Transaction findTransactionById(Long accountId, Long transactionId, String token) throws ResourceNotFoundException, ForbiddenException, JSONException {
        Account account = checkId(accountId);
        validateAccountBelongsUser(account, token);

        return transactionService.findTransactionById(accountId, transactionId);
    }

    //* ///////// CARDS ///////// *//
    @Transactional
    @Override
    public CardGetDTO addCard(Long id, CardPostDTO cardPostDTO, String token) throws ResourceNotFoundException, AlreadyRegisteredException, BadRequestException, ForbiddenException, JSONException {
        Account account = checkId(id);
        validateAccountBelongsUser(account, token);

        return cardService.createCard(account, cardPostDTO);
    }

    @Override
    public List<CardGetDTO> listAllCards(Long id, String token) throws ResourceNotFoundException, ForbiddenException, JSONException {
        Account account = checkId(id);
        validateAccountBelongsUser(account, token);

        return cardService.listCards(account);
    }

    @Override
    public CardGetDTO findCardFromAccount(Long id, Long cardId, String token) throws ResourceNotFoundException, ForbiddenException, JSONException {
        Account account = checkId(id);
        validateAccountBelongsUser(account, token);
        return cardService.findCardById(account, cardId);
    }

    @Override
    public void removeCardFromAccount(Long id, Long cardId, String token) throws ResourceNotFoundException, ForbiddenException, JSONException {
        Account account = checkId(id);
        validateAccountBelongsUser(account, token);

        cardService.deleteCard(account, cardId);
    }

    //* ///////// UTILS ///////// *//
    @Override
    public Account checkId(Long id) throws ResourceNotFoundException {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException(msjIdError + " id: " + id);
        }
        return account.get();
    }

    private String decodeToken(String token) throws JSONException {
        String[] jwtParts = token.split("\\.");
        JSONObject payload = new JSONObject(new String(Base64.getUrlDecoder().decode(jwtParts[1])));
        return payload.getString("user_id");
    }

    private void validateAccountBelongsUser(Account account, String token) throws JSONException, ForbiddenException {
        String userId = decodeToken(token);
        Long userIdL = Long.valueOf(userId);
        if (!account.getUserId().equals(userIdL)) {
            throw new ForbiddenException("You don't have access to that account");
        }
    }

    @Transactional
    @Override
    public CardTransactionGetDTO depositMoney(Long id, CardTransactionPostDTO cardTransactionPostDTO) throws ResourceNotFoundException, PaymentRequiredException, ForbiddenException, BadRequestException {
        return transactionService.processCardTransaction(id, cardTransactionPostDTO);
    }

}
