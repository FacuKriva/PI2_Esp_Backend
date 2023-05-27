package com.digital.money.msvc.api.users.exceptions;

public class CardNotFoundException extends Exception{
    public CardNotFoundException(String mensaje){
        super(mensaje);
    }
}
