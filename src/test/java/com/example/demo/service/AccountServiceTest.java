package com.example.demo.service;

import com.example.demo.commons.AccountType;
import com.example.demo.commons.AccountStatus;
import com.example.demo.domainmapper.AccountDTOToAccountMapper;
import com.example.demo.domainmapper.AccountToAccountDTOMapper;
import com.example.demo.dto.AccountDTO;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.factory.DomainFactory;
import com.example.demo.domain.Account;
import com.example.demo.repo.AccountRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private  AccountRepo accountRepo;
    @Mock
    private  AccountDTOToAccountMapper accountDTOToAccountMapper;
    @Mock
    private  AccountToAccountDTOMapper accountToAccountDTOMapper;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        reset(accountDTOToAccountMapper,accountToAccountDTOMapper,accountRepo);
    }

    @Test
    void test_createAccount() {
        AccountDTO accountDTO = DomainFactory.createAccountDTO();
        Account account = DomainFactory.createAccount();
        when(accountRepo.save(any(Account.class))).thenReturn(account);
        when(accountDTOToAccountMapper.apply(any(AccountDTO.class))).thenReturn(account);
        when(accountToAccountDTOMapper.apply(any(Account.class))).thenReturn(accountDTO);
        final AccountDTO createdAccount = accountService.createAccount(accountDTO);
        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getIban()).isEqualTo(account.getIban());
        verify(accountRepo,times(1)).save(any(Account.class));
    }

    @Test
    void fetchBalance() {
    }

    @Test
    void test_getAccount_accountAvailable() {
        Account account = DomainFactory.createAccount();
        AccountDTO accountDTO = DomainFactory.createAccountDTO();
        when(accountRepo.findById(any(UUID.class))).thenReturn(Optional.of(account));
        when(accountToAccountDTOMapper.apply(any(Account.class))).thenReturn(accountDTO);
        final AccountDTO fetchedAccount = accountService.getAccount(UUID.randomUUID());
        assertThat(fetchedAccount).isNotNull();
        assertThat(fetchedAccount.getAccountType()).isEqualTo(accountDTO.getAccountType());
    }

    @Test
    void test_getAccount_accountNotAvailable(){
        when(accountRepo.findById(any(UUID.class))).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class,() -> accountService.getAccount(UUID.randomUUID()));
    }


    @Test
    void test_unLockAccount_Success() {
        Account account = DomainFactory.createAccount();
        AccountDTO accountDTO = DomainFactory.createAccountDTO();
        account.setAccountStatus(AccountStatus.LOCKED);
        when(accountRepo.findById(any(UUID.class))).thenReturn(Optional.of(account));
        when(accountRepo.save(any(Account.class))).thenReturn(account);
        when(accountToAccountDTOMapper.apply(account)).thenReturn(accountDTO);
        AccountDTO unLockedAccount = accountService.unLockAccount(UUID.randomUUID());
        assertThat(unLockedAccount).isNotNull();
        assertThat(unLockedAccount.getAccountStatus()).isEqualTo(AccountStatus.NOT_LOCKED);
        verify(accountRepo,times(1)).save(any(Account.class));
        verify(accountRepo,times(1)).findById(any(UUID.class));
    }

    @Test
    void test_unLockAccount_throwAccount_NotFound() {
        when(accountRepo.findById(any(UUID.class))).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class,() -> accountService.unLockAccount(UUID.randomUUID()));
    }

    @Test
    void test_fetchAccountsByType_Success() {
        List<Account> accountList = DomainFactory.createListOfAccount();
        AccountDTO accountDTO = DomainFactory.createAccountDTO();
        when(accountRepo.findByAccountType(any(AccountType.class))).thenReturn(accountList);
        when(accountToAccountDTOMapper.apply(any(Account.class))).thenReturn(accountDTO);
        assertThat(accountService.fetchAccountsByType(AccountType.SAVINGS_ACCOUNT)).isNotNull();
        verify(accountRepo,times(1)).findByAccountType(any(AccountType.class));
    }
}