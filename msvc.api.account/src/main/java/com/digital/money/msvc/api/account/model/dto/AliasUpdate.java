package com.digital.money.msvc.api.account.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AliasUpdate {
    @NotNull(message = "The first word cannot be null")
    @NotEmpty(message = "The first word cannot be empty")
    @Size(min = 6, max = 12, message = "The word must contain a minimum of 6 and a maximum of 12 characters")
    private String wordIndexOne;

    @NotNull(message = "The second word cannot be null")
    @NotEmpty(message = "The second word cannot be empty")
    @Size(min = 6, max = 12, message = "The word must contain a minimum of 6 and a maximum of 12 characters")
    private String wordIndexZero;

    @NotNull(message = "The third word cannot be null")
    @NotEmpty(message = "The third word cannot be empty")
    @Size(min = 6, max = 12, message = "The word must contain a minimum of 6 and a maximum of 12 characters")
    private String wordIndexTwo;

    public String buildAlias() {
        return wordIndexZero.concat(".")
                .concat(wordIndexOne).concat(".")
                .concat(wordIndexTwo);
    }
}
