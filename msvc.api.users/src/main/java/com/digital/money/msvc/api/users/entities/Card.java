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

    @Column(name = "cardNumber", unique = true, nullable = false, length = 16)
    private Long cardNumber;

    @Column(name = "cardHolder", nullable = false, length = 30)
    private String cardHolder;

    @Column(name = "expirationDate", nullable = false, length = 7)
    private String expirationDate;

    @Column(name = "cvv", nullable = false, length = 3)
    private Integer cvv;

    @Column(name = "bank", nullable = false, length = 30)
    private String bank;

    @Column(name = "cardType", nullable = false)
    private String cardType;

    @Column(name = "isEnabled", nullable = false)
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