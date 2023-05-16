package com.digital.money.msvc.api.users.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonProperty("user_id")
    private Long userId;

    private String name;

    @JsonProperty("last_name")
    private String lastName;

    private String cvu;

    private String alias;

    private Long dni;

    private String email;

    @JsonIgnore
    private String password;

    private Integer phone;

    @JsonIgnore
    private Boolean enabled;

    @JsonIgnore
    private int attempts;

    @JsonIgnore
    private RoleDTO role;
}
