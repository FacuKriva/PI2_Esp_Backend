package com.digital.money.msvc.api.account.model.dto;

import com.digital.money.msvc.api.account.model.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TransactionPostDto {
    @NotNull(message = "The amount cannot be null")
    @NotEmpty(message = "The amount cannot be empty")
    private Double amount;

    @JsonProperty("description")
    @NotNull(message = "The description cannot be null")
    @NotEmpty(message = "The description cannot be empty")
    private String description;

    @JsonProperty("from_account")
    @NotNull(message = "The from_cvu cannot be null")
    @NotEmpty(message = "The from_cvu cannot be empty")
    private String fromAccount;

    @JsonProperty("to_account")
    @NotNull(message = "The to_cvu cannot be null")
    @NotEmpty(message = "The to_cvu cannot be empty")
    private String toAccount;

}
