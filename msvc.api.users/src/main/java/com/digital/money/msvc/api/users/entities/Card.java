package com.digital.money.msvc.api.users.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("card_id")
    @Column(unique = true, name = "card_id")
    private Long cardId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "alias", unique = true)
    private String alias;

    @Column(name = "card_number", unique = true, nullable = false, length = 16)
    private Long cardNumber;

    @Column(name = "card_holder", nullable = false, length = 30)
    private String cardHolder;

    @Column(name = "expiration_date", nullable = false, length = 5)
    private String expirationDate;

    @Column(name = "cvv", nullable = false, length = 3)
    private Integer cvv;

    @Column(name = "bank", nullable = false, length = 30)
    private String bank;

    @Column(name = "card_Type", nullable = false)
    private String cardType;

    private boolean isEnabled = true;


    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", user=" + user +
                ", alias='" + alias + '\'' +
                ", cardNumber=" + cardNumber +
                ", cardHolder='" + cardHolder + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", cvv=" + cvv +
                ", bank='" + bank + '\'' +
                ", cardType='" + cardType + '\'' +
                ", enabled=" + isEnabled +
                '}';
    }
}