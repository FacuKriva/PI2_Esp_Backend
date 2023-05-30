package com.digital.money.msvc.api.account.handler;

import lombok.Getter;

@Getter
public class ResourceNotFoundException  extends Exception{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
