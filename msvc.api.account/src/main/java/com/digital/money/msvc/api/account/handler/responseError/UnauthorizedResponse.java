package com.digital.money.msvc.api.account.handler.responseError;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
public class UnauthorizedResponse {
    private long timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public UnauthorizedResponse(String message, String path) {
        this.timestamp = Calendar.getInstance().getTimeInMillis();
        this.status = 401;
        this.error = "Unauthorized";
        this.message = message;
        this.path = path;
    }
}
