package com.example.demo.repo;

import com.example.demo.domain.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface TransactionRepo extends JpaRepository<FinancialTransaction, UUID> {

    List<FinancialTransaction> findByToAccountOrFromAccount(UUID toAccountID,UUID fromAccountId);
}
