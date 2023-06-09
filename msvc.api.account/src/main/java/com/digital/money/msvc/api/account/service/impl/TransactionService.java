package com.digital.money.msvc.api.account.service.impl;

import com.digital.money.msvc.api.account.handler.*;
import com.digital.money.msvc.api.account.model.*;
import com.digital.money.msvc.api.account.model.dto.*;
import com.digital.money.msvc.api.account.repository.*;
import com.digital.money.msvc.api.account.service.interfaces.ITransactionService;
import com.digital.money.msvc.api.account.utils.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {

    protected TransactionMapper transactionMapper;
    protected AccountMapper accountMapper;
    protected ITransactionRepository transactionRepository;
    protected IAccountRepository accountRepository;
    protected ICardRepository cardRepository;
    protected CardMapper cardMapper;

    @Autowired
    protected TransactionService(TransactionMapper transactionMapper, AccountMapper accountMapper, ITransactionRepository transactionRepository, IAccountRepository accountRepository, ICardRepository cardRepository, CardMapper cardMapper) {
        this.transactionMapper = transactionMapper;
        this.accountMapper = accountMapper;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }


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
    public LastFiveTransactionDto getLastFive(Long id) throws ResourceNotFoundException {

        Optional<Account> account = accountRepository.findById(id);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("The account doesn't exist");
        }
        List<Transaction> list = transactionRepository.getLastFive(id).get();
        return new LastFiveTransactionDto(accountMapper.toAccountGetDto(account.get()), list);
    }

    @Override
    public Transaction checkId(Long id) throws ResourceNotFoundException {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            throw new ResourceNotFoundException(msjIdError + " id: " + id);
        }
        return transaction.get();
    }

    @Transactional
    @Override
    public CardTransactionGetDTO processCardTransaction(Long id, CardTransactionPostDTO cardTransactionPostDTO) throws ResourceNotFoundException, UnauthorizedException, PaymentRequiredException {
        Card card = cardRepository.findByCardId(cardTransactionPostDTO.getCardId())
                .orElseThrow(() -> new ResourceNotFoundException("The card doesn't exist"));

        CardGetDTO cardGetDTO = cardMapper.toCardGetDTO(card);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The account doesn't exist"));

        if (!card.getAccount().getAccountId().equals(id)) {
            throw new UnauthorizedException("The card doesn't belong to the account");
        }
        if (card.getCardBalance() < cardTransactionPostDTO.getAmount()) {
            throw new PaymentRequiredException("The card doesn't have enough balance");
        }

        Transaction transaction = transactionMapper.cTPDTOToTransaction(cardTransactionPostDTO);
        transaction.setAmount(cardTransactionPostDTO.getAmount());
        transaction.setRealizationDate(LocalDateTime.now());
        transaction.setDescription("You deposited $" + cardTransactionPostDTO.getAmount() + " from " +
                cardGetDTO.getBank() + " " + card.getCardType());
        transaction.setFromCvu(String.valueOf(card.getCardNumber()));
        transaction.setToCvu(account.getCvu());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAccount(account);

        card.setCardBalance(card.getCardBalance() - cardTransactionPostDTO.getAmount());

        account.setAvailableBalance(account.getAvailableBalance() + cardTransactionPostDTO.getAmount());

        cardRepository.save(card);

        accountRepository.save(account);

        transactionRepository.save(transaction);

        CardTransactionGetDTO cTGDTO = transactionMapper.transactionToCardTransactionGetDTO(transaction);
        cTGDTO.setCardNumber(cardGetDTO.getCardNumber());

        return cTGDTO;
    }
}
