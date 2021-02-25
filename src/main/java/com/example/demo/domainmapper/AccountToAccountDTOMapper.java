package com.example.demo.domainmapper;

import com.example.demo.commons.MoneyValue;
import com.example.demo.dto.AccountDTO;
import com.example.demo.domain.Account;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AccountToAccountDTOMapper implements Function<Account, AccountDTO> {

    @Override
    public AccountDTO apply(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIban(account.getIban());
        accountDTO.setAccountType(account.getAccountType());
        accountDTO.setMoneyValue(MoneyValue.builder().amount(account.getBalance()).currency(account.getCurrency()).build());
        accountDTO.setAccountStatus(account.getAccountStatus());
        return accountDTO;
    }
}
