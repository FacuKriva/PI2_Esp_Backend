package com.digital.money.msvc.api.users.services;

import com.digital.money.msvc.api.users.entities.User;
import com.digital.money.msvc.api.users.entities.Verified;

public interface IVerificationService {

    Integer createVerificationCode(Long userId);
    boolean verificateCode(Verified verified);

}