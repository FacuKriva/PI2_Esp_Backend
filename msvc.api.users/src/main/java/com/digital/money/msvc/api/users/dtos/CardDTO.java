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

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("alias")
    private String alias;

    @JsonProperty("card_number")
    private Long cardNumber;

    @JsonIgnore
    private String cardHolder;

    @JsonProperty("expiration_date")
    private String expirationDate;

    @JsonIgnore
    private Integer cvv;

    @JsonProperty("bank")
    private String bank;

    @JsonProperty("card_type")
    private String cardType;

    @JsonIgnore
    private boolean isEnabled;
}
