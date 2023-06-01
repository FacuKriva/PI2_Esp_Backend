package com.digital.money.msvc.api.account.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardPostDTO {

    @JsonProperty("alias")
    @NotEmpty(message = "The alias cannot be empty")
    private String alias;

    @JsonProperty("cardNumber")
    @NotEmpty(message = "The cardNumber cannot be empty")
    private Long cardNumber;

    @JsonProperty("cardHolder")
    @NotEmpty(message = "The cardHolder cannot be empty")
    private String cardHolder;

    @JsonProperty("expirationDate")
    @NotEmpty(message = "The expirationDate cannot be empty")
    private String expirationDate;

    @JsonProperty("cvv")
    @NotNull(message = "The cvv cannot be empty")
    private Integer cvv;

    @JsonProperty("bank")
    @NotEmpty(message = "The bank cannot be empty")
    private String bank;

    @JsonProperty("cardType")
    @NotEmpty(message = "The cardType cannot be empty")
    private String cardType;
}
