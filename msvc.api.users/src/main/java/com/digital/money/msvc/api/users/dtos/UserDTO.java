package com.digital.money.msvc.api.users.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    private List<CardDTO> cards;

    @JsonIgnore
    private Boolean enabled;

    @JsonIgnore
    private int attempts;

    @JsonIgnore
    private RoleDTO role;

    @JsonIgnore
    private Boolean verified;
}
