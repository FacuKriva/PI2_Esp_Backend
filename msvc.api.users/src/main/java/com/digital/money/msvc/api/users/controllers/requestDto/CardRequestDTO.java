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

    @NotNull(message = "The card_number cannot be null")
    @Size( message = "There must be 16 numbers for the card_number", min = 16, max = 16)
    private Long cardNumber;

    @NotNull(message = "The card_holder cannot be null")
    @Size(max = 70, message = "maximum number of characters 70")
    private String cardHolder;

    @NotNull(message = "The expiration_date cannot be null")
    @DateTimeFormat(pattern = "MM/yy")
    private String expirationDate;

    @NotNull(message = "The cvv cannot be null")
    @Size( message = "There must be 3 numbers for the cvv", min = 3, max = 3)
    private Integer cvv;

    @NotNull(message = "The bank cannot be null or empty")
    private String bank;
}