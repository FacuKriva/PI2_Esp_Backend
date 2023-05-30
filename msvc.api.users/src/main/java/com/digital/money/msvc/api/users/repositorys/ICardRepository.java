package com.digital.money.msvc.api.users.repositorys;

import com.digital.money.msvc.api.users.entities.Card;
import com.digital.money.msvc.api.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByCardNumber(Long cardNumber);
    List<Card> findByUser(User user);
    Optional<Card> findById(Long cardId);
}
