package com.digital.money.msvc.api.account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("account_id")
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "cvu", unique = true, nullable = false, length = 22)
    private String cvu;

    @Column(name = "alias", unique = true, nullable = false)
    private String alias;

    @Column(name = "available_balance")
    private Double availableBalance;

    @JsonProperty("user_id")
    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private Set<Transaction> transactions;
}
