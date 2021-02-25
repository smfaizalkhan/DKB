package com.example.demo.domainmapper;

import com.example.demo.dto.AccountDTO;
import com.example.demo.domain.Account;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AccountDTOToAccountMapper implements Function<AccountDTO, Account> {

    @Override
    public Account apply(AccountDTO accountDTO) {
        Account account = new Account();
        account.setAccountType(accountDTO.getAccountType());
        account.setAccountStatus(accountDTO.getAccountStatus());
        account.setCurrency(accountDTO.getMoneyValue().getCurrency());
        account.setBalance(accountDTO.getMoneyValue().getAmount());
        return account;
    }
}
