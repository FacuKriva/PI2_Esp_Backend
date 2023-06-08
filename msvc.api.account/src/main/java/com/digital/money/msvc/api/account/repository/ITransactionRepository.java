package com.digital.money.msvc.api.account.repository;

import com.digital.money.msvc.api.account.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :id ORDER BY t.realizationDate DESC LIMIT 5")
    Optional<List<Transaction>> getLastFive(@Param("id") Long id);
    Optional<Transaction> findByAccount_AccountIdAndTransactionId(Long accountId, Long transactionId);

}
