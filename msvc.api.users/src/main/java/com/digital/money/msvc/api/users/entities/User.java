package com.digital.money.msvc.api.users.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("user_id")
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @JsonProperty("last_name")
    @Column(name = "last_name", length = 40, nullable = false)
    private String lastName;

    @Column(name = "cvu", unique = true, nullable = false, length = 22)
    private String cvu;

    @Column(name = "alias", unique = true, nullable = false)
    private String alias;

    @Column(name = "dni", unique = true, length = 10, nullable = false)
    private Long dni;

    @Column(name = "email", length = 60, unique = true, nullable = false)
    private String email;

    @Column(name = "password", length = 120, nullable = false)
    private String password;

    @Column(name = "phone", length = 10, nullable = false)
    private Integer phone;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}