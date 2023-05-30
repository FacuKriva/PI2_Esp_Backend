package com.digital.money.msvc.api.users.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {

    @JsonProperty("card_id")
    private Long cardId;

     @JsonIgnore
    private UserDTO user;

    @JsonProperty("alias")
    private String alias;

    @JsonProperty("cardNumber")
    private Long cardNumber;

    @JsonIgnore
    private String cardHolder;

    private String expirationDate;

    @JsonIgnore
    private Integer cvv;

    @JsonProperty("bank")
    private String bank;

    private String cardType;

    @JsonIgnore
    private boolean isEnabled;
}
