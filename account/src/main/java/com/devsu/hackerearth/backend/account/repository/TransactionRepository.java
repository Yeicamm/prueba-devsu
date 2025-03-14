package com.devsu.hackerearth.backend.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);
    
    @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId ORDER BY t.date DESC")
    Transaction findLastTransaction(@Param("accountId") Long accountId);

}
