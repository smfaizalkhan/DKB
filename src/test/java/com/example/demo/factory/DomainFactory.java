package com.example.demo.factory;

import com.example.demo.commons.AccountType;
import com.example.demo.commons.AccountStatus;
import com.example.demo.commons.MoneyValue;
import com.example.demo.commons.TransactionType;
import com.example.demo.dto.AccountDTO;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.domain.Account;
import com.example.demo.domain.FinancialTransaction;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DomainFactory {

    private static final UUID iban = UUID.randomUUID();
    private static final UUID fromAccountNo = createListOfAccount().get(0).getIban();
    private static final UUID toAccountNo = createListOfAccount().get(1).getIban();


    public static TransactionDTO createTransactionDTO() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(MoneyValue.builder().amount("1000.345").currency("EUR").build());
        transactionDTO.setToAccount(toAccountNo);
        transactionDTO.setFromAccount(fromAccountNo);
        transactionDTO.setTransactionType(TransactionType.DEPOSIT);
        transactionDTO.setTransactionId(UUID.randomUUID());
        return transactionDTO;
    }

    public static TransactionDTO createTransactionDTOWithEmptyFromValue() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(MoneyValue.builder().amount("1000.345").currency("EUR").build());
        transactionDTO.setToAccount(toAccountNo);
        transactionDTO.setTransactionType(TransactionType.DEPOSIT);
        transactionDTO.setTransactionId(UUID.randomUUID());
        return transactionDTO;
    }

    public static FinancialTransaction createFinancialTransaction() {
        FinancialTransaction financialTransaction = new FinancialTransaction();
        financialTransaction.setUuid(UUID.randomUUID());
        financialTransaction.setTransactionType(TransactionType.DEPOSIT);
        financialTransaction.setCurrency("EUR");
        financialTransaction.setAmount("1000.345");
        financialTransaction.setExecutedDate(LocalDateTime.now());
        financialTransaction.setFromAccount(fromAccountNo);
        financialTransaction.setToAccount(toAccountNo);
        return financialTransaction;
    }

    public static Account createAccount() {
        Account account = new Account();
        account.setBalance("123456.789");
        account.setAccountStatus(AccountStatus.NOT_LOCKED);
        account.setCurrency("EUR");
        account.setIban(iban);
        account.setAccountType(AccountType.SAVINGS_ACCOUNT);
        return account;
    }

    public static AccountDTO createAccountDTO() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountType(AccountType.SAVINGS_ACCOUNT);
        accountDTO.setAccountStatus(AccountStatus.NOT_LOCKED);
        accountDTO.setIban(iban);
        accountDTO.setMoneyValue(MoneyValue.builder().amount("123456.789").currency("EUR").build());
        return accountDTO;
    }

    public static List<Account> createListOfAccount() {

        return IntStream.rangeClosed(0,10).mapToObj(i ->
        {
            Account account = new Account();
            account.setIban(UUID.randomUUID());
            account.setAccountStatus(calculateAccountStatus(i));
            account.setBalance("10000.30");
            account.setCurrency("EUR");
            account.setAccountType(calculateAccountType(i));
            return account;
        }).collect(Collectors.toList());
    }

    private static AccountStatus calculateAccountStatus(int i) {
        if(i%2==0)
            return AccountStatus.NOT_LOCKED;
        else if(i%5==0 || (i%3==0))return AccountStatus.NOT_LOCKED;
        else return AccountStatus.LOCKED;
    }

    private static AccountType calculateAccountType(int i) {
        if(i%2==0){
            return AccountType.SAVINGS_ACCOUNT;
        }else if(i%5==0){
            return AccountType.PRIVATE_LOAN_ACCOUNT;
        }
        else return AccountType.CHECKING_ACCOUNT;
    }

    public static List<FinancialTransaction> createListOfFinancialTransaction() {
        return Arrays.asList(createFinancialTransaction());
    }
}
