package com.restassured.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    private String alias;
    private Long cardNumber;
    private String cardHolder;
    private String expirationDate;
    private Integer cvv;
    private String bank;
    private String cardType;

}
