package com.digital.money.msvc.api.account.model.dto;

import com.digital.money.msvc.api.account.model.Account;
import com.digital.money.msvc.api.account.model.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TransactionGetDto {
    private Long transactionId;

    private Double amount;

    private Date realizationDate;

    private String description;

    private String fromCvu;

    private String toCvu;

    private TransactionType type;

    private Account account;
}
