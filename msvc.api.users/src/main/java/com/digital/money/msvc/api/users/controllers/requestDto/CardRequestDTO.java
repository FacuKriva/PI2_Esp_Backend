package com.digital.money.msvc.api.users.controllers.requestDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardRequestDTO {

    @NotNull(message = "The alias cannot be null")
    private String alias;

    @NotNull(message = "The cardNumber cannot be null")
    @Size( message = "There must be 16 numbers for the cardNumber", min = 15, max = 16)
    private Long cardNumber;

    @NotNull(message = "The cardHolder cannot be null")
    @Size(max = 70, message = "maximum number of characters 70")
    private String cardHolder;

    @NotNull(message = "The expirationDate cannot be null")
    @DateTimeFormat(pattern = "MM/yyyy")
    private String expirationDate;

    @NotNull(message = "The cvv cannot be null")
    @Size( message = "There must be 4 numbers for the cvv", min = 4, max = 4)
    private Integer cvv;

    @NotNull(message = "The bank cannot be null or empty")
    private String bank;

    @NotNull(message = "The cardType cannot be null or empty")
    private String cardType;
}