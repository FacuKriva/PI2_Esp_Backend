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
public class AuthUserDTO {

    @JsonProperty("user_id")
    private Long userId;

    private String name;

    @JsonProperty("last_name")
    private String lastName;

    private String cvu;

    private String alias;

    private Long dni;

    private String email;

    private String password;

    private Integer phone;

    private Boolean enabled;

    private int attempts;

    private RoleDTO role;
}