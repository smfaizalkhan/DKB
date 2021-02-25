package com.example.demo.dto;

import com.example.demo.commons.AccountType;
import com.example.demo.commons.AccountStatus;
import com.example.demo.commons.MoneyValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private UUID iban;
    private AccountType accountType;
    private MoneyValue moneyValue;
    private AccountStatus accountStatus ;
}
