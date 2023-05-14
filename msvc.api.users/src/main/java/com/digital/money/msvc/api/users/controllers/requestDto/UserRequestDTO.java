package com.digital.money.msvc.api.users.controllers.requestDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class UserRequestDTO {

    @NotNull(message = "The name cannot be null or empty")
    @Size(max = 30, message = "maximum number of characters 30")
    private String name;

    @JsonProperty("last_name")
    @NotNull(message = "The last_name cannot be null or empty")
    @Size(max = 40, message = "maximum number of characters 40")
    private String lastName;

    @NotNull(message = "The dni cannot be null or empty")
    @Size(max = 10, message = "maximum number of characters 10")
    private Long dni;

    @Email
    @NotBlank(message = "the email cannot be null or empty")
    @Size(max = 60, message = "maximum number of characters 60")
    private String email;

    @NotBlank(message = "The password cannot be null or empty ")
    @Size(max = 30, message = "maximum number of characters 30")
    private String password;

    @NotBlank(message = "The phone cannot be null or empty ")
    @Size(max = 10, message = "maximum number of characters 10")
    private Integer phone;

    @JsonProperty("role_id")
    @NotNull(message = "The role_id cannot be null")
    private int roleId;
}
