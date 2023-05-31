package com.digital.money.msvc.api.account.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CardPostDTO {

    @JsonProperty("alias")
    @NotNull(message = "The alias cannot be empty")
    @Size(max = 50, message = "maximum number of characters 50")
    private String alias;

    @JsonProperty("cardNumber")
    @NotNull(message = "The cardNumber cannot be empty")
    @Size( message = "There must be at least 15 numbers for the cardNumber", min = 15)
    @Size( message = "There must be at most 16 numbers for the cardNumber", max = 16)
    private Long cardNumber;

    @JsonProperty("cardHolder")
    @NotNull(message = "The cardHolder cannot be empty")
    @Size(max = 70, message = "maximum number of characters 70")
    private String cardHolder;

    @JsonProperty("expirationDate")
    @NotNull(message = "The expirationDate cannot be empty")
    @Size(max = 6, message = "maximum number of characters 7")
    @DateTimeFormat(pattern = "MM/yyyy")
    private String expirationDate;

    @JsonProperty("cvv")
    @NotNull(message = "The cvv cannot be empty")
    @Size( message = "There must be at least 3 numbers for the cvv", min = 3)
    @Size( message = "There must be at most 4 numbers for the cvv", max = 4)
    private Integer cvv;

    @JsonProperty("bank")
    @NotNull(message = "The bank cannot be empty")
    @Size(max = 50, message = "maximum number of characters 50")
    private String bank;

    @JsonProperty("cardType")
    @NotNull(message = "The cardType cannot be empty")
    @Size(max = 16, message = "maximum number of characters 16")
    private String cardType;
}
