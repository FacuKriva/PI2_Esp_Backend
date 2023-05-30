package com.digital.money.msvc.api.account.handler;

public class NoTransactionsException extends Exception {
    public NoTransactionsException(String message){
        super(message);
    }
}
