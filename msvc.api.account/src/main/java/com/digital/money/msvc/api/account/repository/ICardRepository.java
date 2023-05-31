package com.digital.money.msvc.api.account.repository;

import com.digital.money.msvc.api.account.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICardRepository extends JpaRepository<Card, Long> {

        Optional<Card> findByCardById(Long cardId);
        Optional<Card> findByCardNumber(Long cardNumber);
        Optional<List<Card>> findAllByAccountId(Long accountId);
}
