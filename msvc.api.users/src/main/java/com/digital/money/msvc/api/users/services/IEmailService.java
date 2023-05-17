package com.digital.money.msvc.api.users.services;

import com.digital.money.msvc.api.users.entities.User;

public interface IEmailService {

    void sendMail(User user, Integer codigo);

    void sendForgotPasswordEmail(User user, String link);
}
