package com.digital.money.msvc.api.account.service.impl;

import com.digital.money.msvc.api.account.handler.ResourceNotFoundException;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.Transaction;
import com.digital.money.msvc.api.account.model.TransactionType;
import com.digital.money.msvc.api.account.model.dto.LastFiveTransactionDto;
import com.digital.money.msvc.api.account.model.dto.TransactionGetDto;
import com.digital.money.msvc.api.account.model.dto.TransactionPostDto;
import com.digital.money.msvc.api.account.repository.IAccountRepository;
import com.digital.money.msvc.api.account.repository.ITransactionRepository;
import com.digital.money.msvc.api.account.service.interfaces.ITransactionService;
import com.digital.money.msvc.api.account.utils.mapper.AccountMapper;
import com.digital.money.msvc.api.account.utils.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    protected TransactionMapper transactionMapper;
    @Autowired
    protected AccountMapper accountMapper;
    @Autowired
    protected ITransactionRepository transactionRepository;
    @Autowired
    protected IAccountRepository accountRepository;

    @Transactional
    @Override
    public TransactionGetDto save(TransactionPostDto transactionPostDto) {
        Transaction transaction = transactionMapper.toTransaction(transactionPostDto);
        if (accountRepository.findByCvu(transaction.getToCvu()).get().getAccountId() == transaction.getAccount().getAccountId()) {
            transaction.setType(TransactionType.INCOMING);
        } else {
            transaction.setType(TransactionType.OUTGOING);
        }
        transactionRepository.save(transaction);
        return transactionMapper.toTransactionGetDto(transaction);
    }

    @Transactional(readOnly = true)
    public LastFiveTransactionDto getLastFive(Long id, Account account) throws ResourceNotFoundException {
        List<Transaction> list = transactionRepository.getLastFive(id).get();
        return new LastFiveTransactionDto(accountMapper.toAccountGetDto(account), list);
    }

    @Override
    public Transaction checkId(Long id) throws ResourceNotFoundException {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            throw new ResourceNotFoundException(msjIdError + " id: " + id);
        }
        return transaction.get();
    }

    @Override
    public Transaction findTransactionById(Long accountId, Long transactionId) throws ResourceNotFoundException {

        Optional<Transaction> transaction = transactionRepository.findByAccount_AccountIdAndTransactionId(accountId,transactionId);

        if (transaction.isEmpty()) {
            throw new ResourceNotFoundException("Not transference found for that id");
        }
        return transaction.get();
    }
}
