package com.example.demo.repo;


import com.example.demo.commons.AccountType;
import com.example.demo.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepo extends JpaRepository<Account, UUID> {
    List<Account> findByAccountType(AccountType accountType);
}
