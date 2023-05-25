package com.digital.money.msvc.api.users.controllers.requestDto.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class Alias {

    @NotNull(message = "The first word cannot be null or empty")
    @Size(min = 6, max = 10, message = "The word must contain a minimum of 6 and a maximum of 1 characters")
    private String wordIndexOne;

    @NotNull(message = "The second word cannot be null or empty")
    @Size(min = 6, max = 10, message = "The word must contain a minimum of 6 and a maximum of 1 characters")
    private String wordIndexZero;

    @NotNull(message = "The third word cannot be null or empty")
    @Size(min = 6, max = 10, message = "The word must contain a minimum of 6 and a maximum of 1 characters")
    private String wordIndexTwo;

    public String buildAlias() {
        return wordIndexZero.concat(".")
                .concat(wordIndexOne).concat(".")
                .concat(wordIndexTwo);
    }
}
