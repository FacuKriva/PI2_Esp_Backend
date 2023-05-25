package com.digital.money.msvc.api.users.controllers.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDTO {

    @Size(max = 30, message = "maximum number of characters 30")
    private String name;

    @JsonProperty("last_name")
    @Size(max = 40, message = "maximum number of characters 40")
    private String lastName;

    private Long dni;

    @Email
    @Size(max = 60, message = "maximum number of characters 60")
    private String email;

    @Size(min = 8, max = 30, message = "minimum number of characters 8, maximum number of characters 30")
    private String password;

    @NotNull(message = "The phone cannot be null or empty ")
    private Integer phone;
}
