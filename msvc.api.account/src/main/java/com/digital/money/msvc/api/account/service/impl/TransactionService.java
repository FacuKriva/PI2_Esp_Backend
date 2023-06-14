package com.digital.money.msvc.api.account.service.impl;

import com.digital.money.msvc.api.account.handler.*;
import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.Card;
import com.digital.money.msvc.api.account.model.Transaction;
import com.digital.money.msvc.api.account.model.TransactionType;
import com.digital.money.msvc.api.account.model.dto.*;
import com.digital.money.msvc.api.account.repository.IAccountRepository;
import com.digital.money.msvc.api.account.repository.ICardRepository;
import com.digital.money.msvc.api.account.repository.ITransactionRepository;
import com.digital.money.msvc.api.account.service.ITransactionService;
import com.digital.money.msvc.api.account.utils.mapper.AccountMapper;
import com.digital.money.msvc.api.account.utils.mapper.CardMapper;
import com.digital.money.msvc.api.account.utils.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
    @Override
    public ListTransactionDto getLastFive(Long id, Account account){
        List<Transaction> list = transactionRepository.getLastFive(id).get();
        return new ListTransactionDto(accountMapper.toAccountGetDto(account), list);
    }

    @Override
    public Transaction findTransactionById(Long accountId, Long transactionId) throws ResourceNotFoundException {

        Optional<Transaction> transaction = transactionRepository.findByAccount_AccountIdAndTransactionId(accountId,transactionId);

        if (transaction.isEmpty()) {
            throw new ResourceNotFoundException("Not transference found for that id");
        }
        return transaction.get();
    }

    @Override
    public ListTransactionDto findAllSorted(Long id, Account account) {
        List<Transaction> list = transactionRepository.findAllSorted(id).get();
        return new ListTransactionDto(accountMapper.toAccountGetDto(account), list);
    }

    //* ///////// UTILS ///////// *//
    @Override
    public Transaction checkId(Long id) throws ResourceNotFoundException {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            throw new ResourceNotFoundException(msjIdError + " id: " + id);
        }
        return transaction.get();
    }

    private boolean isExpirationDateValid(String expirationDate) {
        try {
            YearMonth yearMonth = YearMonth.parse(expirationDate, cardMapper.formatter);
            YearMonth currentYearMonth = YearMonth.now();
            return yearMonth.isAfter(currentYearMonth) || yearMonth.equals(currentYearMonth);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Transactional
    @Override
    public CardTransactionGetDTO processCardTransaction(Long id, CardTransactionPostDTO cardTransactionPostDTO) throws ResourceNotFoundException, ForbiddenException, PaymentRequiredException, BadRequestException {

        Card card = cardRepository.findByCardId(cardTransactionPostDTO.getCardId())
                .orElseThrow(() -> new ResourceNotFoundException("The card doesn't exist"));

        CardGetDTO cardGetDTO = cardMapper.toCardGetDTO(card);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The account doesn't exist"));

        if (!card.getAccount().getAccountId().equals(id)) {
            throw new ForbiddenException("The card doesn't belong to the account");
        }
//        if (card.getCardBalance() < cardTransactionPostDTO.getAmount()) {
//            throw new PaymentRequiredException("The card doesn't have enough balance");
//        }

       if (!isExpirationDateValid(card.getExpirationDate())) {
           throw new BadRequestException("The card you are trying to use is expired. ");
       }

        if (cardTransactionPostDTO.getAmount() == 0.0) {
            throw new BadRequestException("The amount can't be 0. Please enter a valid amount");
        } else if (cardTransactionPostDTO.getAmount() < 0.0) {
            throw new BadRequestException("The amount can't be negative. Please enter a valid amount");
        }

        Transaction transaction = transactionMapper.cTPDTOToTransaction(cardTransactionPostDTO);
        transaction.setAmount(cardTransactionPostDTO.getAmount());
        transaction.setRealizationDate(LocalDateTime.now());
        transaction.setDescription("You deposited $" + cardTransactionPostDTO.getAmount() + " from " +
                cardGetDTO.getBank() + " " + card.getCardType());
        transaction.setFromCvu(String.valueOf(card.getCardNumber()));
        transaction.setToCvu(account.getCvu());
        transaction.setType(TransactionType.INCOMING);
        transaction.setAccount(account);

        //card.setCardBalance(card.getCardBalance() - cardTransactionPostDTO.getAmount());

        account.setAvailableBalance(account.getAvailableBalance() + cardTransactionPostDTO.getAmount());

        cardRepository.save(card);

        accountRepository.save(account);

        transactionRepository.save(transaction);

        CardTransactionGetDTO cTGDTO = transactionMapper.transactionToCardTransactionGetDTO(transaction);
        cTGDTO.setCardNumber(cardGetDTO.getCardNumber());

        return cTGDTO;
    }

    @Override
    public List<Transaction> getAllTransactionsByAmountRange(Integer rangoSelected, Long accountId) throws Exception {
        Double[] rangos = {0.0,0.0,1000.0,5000.0,20000.0,100000.0};
        List<Transaction> transactions = new ArrayList<>();

        if(rangoSelected<=0 || rangoSelected>5){
            throw new SelectOutOfBoundException("Please select a option within the range");
        }

        Double firstR = rangos[rangoSelected];


        if(rangoSelected==5){
            transactions = transactionRepository.findByAmountGreaterThanEqualAndAccount_AccountId(firstR,accountId);
        }else{
            Double secondR = rangos[rangoSelected+1];
            transactions = transactionRepository.findByAmountBetweenAndAccount_AccountId(firstR,secondR,accountId);
        }

        return transactions;
    }

    @Value("${spring.datasource.url}")
    private String urlDB;

    @Value("${spring.datasource.username}")
    private String userDB;

    @Value("${spring.datasource.password}")
    private String passDB;
    @Override
    public List<Transaction> getTransactionsWithFilters(String startDate, String endDate, Integer rangeSelect, String type) throws Exception{

        String query = "SELECT * FROM transactions WHERE";
        Boolean ampersand = false;
        ArrayList<Object> params = new ArrayList<Object>();

        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println(rangeSelect);
        System.out.println(type);


        if(startDate!=null){
            LocalDateTime startDateLDT, endDateLDT;
            startDateLDT = LocalDateTime.parse(startDate);

            if(endDate!=null){
                endDateLDT = LocalDateTime.parse(endDate);
            }
            else {
                endDateLDT = LocalDateTime.now();
            }
            params.add(startDateLDT);
            params.add(endDateLDT);

            query+=" realizationDate BETWEEN ? AND ?";
            ampersand=true;
        }

        TransactionType transactionType = TransactionType.INCOMING;

        if(type.equals("INCOMING")){
            transactionType = TransactionType.INCOMING;
        } else if (type.equals("OUTGOING")) {
            transactionType = TransactionType.OUTGOING;
        }

        if (transactionType!=null){
            if(ampersand){
                query+= " AND";
            }
            query+=" type = ?";
            params.add(transactionType);
            ampersand=true;
        }

        Double[] rangos = {0.0,0.0,1000.0,5000.0,20000.0,100000.0};

        if(rangeSelect!=null) {
            if (rangeSelect >= 1 && rangeSelect <= 5) {
                if (ampersand) {
                    query += " AND";
                }
                Double firstR = rangos[rangeSelect];
                params.add(firstR);

                if (rangeSelect == 5) {
                    query += " amount < ?";
                } else {
                    Double secondR = rangos[rangeSelect + 1];
                    query += " amount BETWEEN ? AND ? ";
                    params.add(secondR);
                }

            }
        }

        Object[] parameters = params.toArray();

        System.out.println(query);

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection= DriverManager.getConnection(urlDB,userDB,passDB);


        PreparedStatement preparedStatement = connection.prepareStatement(query);

        for(int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i+1, parameters[i]);
            System.out.println(parameters[i]);
        }

        ResultSet resultSet = preparedStatement.executeQuery();

        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }

        return null;
    }
}
