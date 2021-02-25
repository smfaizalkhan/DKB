package com.example.demo.service;

import com.example.demo.commons.AccountType;
import com.example.demo.commons.AccountStatus;
import com.example.demo.commons.MoneyValue;
import com.example.demo.domainmapper.AccountDTOToAccountMapper;
import com.example.demo.domainmapper.AccountToAccountDTOMapper;
import com.example.demo.dto.AccountDTO;
import com.example.demo.exception.AccountLockedException;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.domain.Account;
import com.example.demo.repo.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepo accountRepo;
    private final AccountDTOToAccountMapper accountDTOToAccountMapper;
    private final AccountToAccountDTOMapper accountToAccountDTOMapper;

    public AccountDTO createAccount(AccountDTO accountDTO){
      Account account = accountDTOToAccountMapper.apply(accountDTO);
      Account createdAccount = accountRepo.save(account);
      return accountToAccountDTOMapper.apply(createdAccount);
    }

    public MoneyValue fetchBalance(UUID accountId) {
        AccountDTO account = getAccount(accountId);
        return account.getMoneyValue();
    }

    public AccountDTO getAccount(UUID accountId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account Not Found For Account Id %s",accountId)));
        return accountToAccountDTOMapper.apply(account);
    }

    public AccountDTO unLockAccount(UUID accountId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new AccountLockedException(String.format("Account Locked For Account Id %s",accountId)));
        account.setAccountStatus(AccountStatus.NOT_LOCKED);
        Account updatedAccount = accountRepo.save(account);
        return accountToAccountDTOMapper.apply(updatedAccount);
    }

    public List<AccountDTO> fetchAccountsByType(AccountType accountType) {
        List<Account> accounts = accountRepo.findByAccountType(accountType);
      return  accounts.stream().map(account -> accountToAccountDTOMapper.apply(account)).collect(Collectors.toList());
    }

    /**
     *   Developer helper methods
     *
     * @return
     */
    public List<AccountDTO> getAccounts() {
        List<Account> accounts = this.initData().stream()
                .map(accountDTO -> accountRepo.save(
                        accountDTOToAccountMapper.apply(accountDTO))).collect(Collectors.toList());

        return accounts.stream().map(account -> accountToAccountDTOMapper.apply(account)).collect(Collectors.toList());
    }

    private List<AccountDTO> initData() {

        return IntStream.rangeClosed(0,10).mapToObj(i ->
        {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setAccountStatus(calculateAccountStatus(i));
            accountDTO.setMoneyValue(MoneyValue.builder().currency("EUR").amount("10098.45").build());
            accountDTO.setAccountType(calculateAccountType(i));
            return accountDTO;
        }).collect(Collectors.toList());
    }

    private AccountStatus calculateAccountStatus(int i) {
        if(i%2==0)
            return AccountStatus.NOT_LOCKED;
        else if(i%5==0 || (i%3==0))return AccountStatus.NOT_LOCKED;
        else return AccountStatus.LOCKED;
    }

    private AccountType calculateAccountType(int i) {
        if(i%2==0){
            return AccountType.SAVINGS_ACCOUNT;
        }else if(i%5==0){
            return AccountType.PRIVATE_LOAN_ACCOUNT;
        }
        else return AccountType.CHECKING_ACCOUNT;
    }
}
