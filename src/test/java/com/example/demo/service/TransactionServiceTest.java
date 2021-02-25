package com.example.demo.service;

import com.example.demo.commons.AccountStatus;
import com.example.demo.commons.TrxType;
import com.example.demo.domainmapper.TransactionDTOToTransactionMapper;
import com.example.demo.domainmapper.TransactionToTransactionDTOMapper;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.exception.AccountLockedException;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.NotEnoughBalanceException;
import com.example.demo.factory.DomainFactory;
import com.example.demo.domain.Account;
import com.example.demo.domain.FinancialTransaction;
import com.example.demo.repo.AccountRepo;
import com.example.demo.repo.TransactionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private  TransactionDTOToTransactionMapper transactionDTOToTransactionMApper;
    @Mock
    private  TransactionToTransactionDTOMapper transactionToTransactionDTOMapper;
    @Mock
    private  TransactionRepo transactionRepo;
    @Mock
    private  AccountRepo accountRepo;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        reset(transactionDTOToTransactionMApper,transactionToTransactionDTOMapper,transactionRepo,accountRepo);
    }

    @Test
    void test_executeDeposit_Success() {
        Account account = DomainFactory.createAccount();
        TransactionDTO transactionDTO = DomainFactory.createTransactionDTO();
        FinancialTransaction financialTransaction = DomainFactory.createFinancialTransaction();
        when(accountRepo.findById(any(UUID.class))).thenReturn(Optional.of(account));
        when(transactionRepo.save(any(FinancialTransaction.class))).thenReturn(financialTransaction);
        when(transactionDTOToTransactionMApper.apply(transactionDTO)).thenReturn(financialTransaction);
        when(transactionToTransactionDTOMapper.apply(any(FinancialTransaction.class))).thenReturn(transactionDTO);
        assertThat(transactionService.executeDeposit(transactionDTO)).isNotNull();
        verify(transactionRepo,times(1)).save(any(FinancialTransaction.class));
        verify(accountRepo,times(1)).findById(any(UUID.class));
    }


    @Test
    void test_executeDeposit_AccountLockedException() {
        TransactionDTO transactionDTO = DomainFactory.createTransactionDTO();
        Account account = DomainFactory.createAccount();
        account.setAccountStatus(AccountStatus.LOCKED);
        when(accountRepo.findById(any(UUID.class))).thenReturn(Optional.ofNullable(account));
        assertThrows(AccountLockedException.class,() -> transactionService.executeDeposit(transactionDTO));
    }

    @Test
    void test_executeTransfer_Success() {
        List<Account> listOfAccount = DomainFactory.createListOfAccount();
        Account fromAccount = listOfAccount.get(0);
        Account toAccount =listOfAccount.get(2);
        TransactionDTO transactionDTO = DomainFactory.createTransactionDTO();
        FinancialTransaction financialTransaction = DomainFactory.createFinancialTransaction();
        when(accountRepo.findById(transactionDTO.getFromAccount())).thenReturn(Optional.of(fromAccount));
        when(accountRepo.findById(transactionDTO.getToAccount())).thenReturn(Optional.of(toAccount));
        when(accountRepo.save(fromAccount)).thenReturn(fromAccount);
        when(accountRepo.save(toAccount)).thenReturn(toAccount);
        when(transactionDTOToTransactionMApper.apply(any(TransactionDTO.class))).thenReturn(financialTransaction);
        when(transactionRepo.save(any(FinancialTransaction.class))).thenReturn(financialTransaction);
        when(transactionToTransactionDTOMapper.apply(any(FinancialTransaction.class))).thenReturn(transactionDTO);
        TransactionDTO trxDto = transactionService.executeTransfer(transactionDTO);
        assertThat(trxDto).isNotNull();
    }

    @Test
    void test_executeTransfer_throwsAccountNotFoundException() {
        TransactionDTO transactionDTO = DomainFactory.createTransactionDTO();
         when(accountRepo.findById(any(UUID.class))).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class,() -> transactionService.executeTransfer(transactionDTO));
    }

    @Test
    void test_executeTransfer_throwsAccountLockedException() {
        TransactionDTO transactionDTO = DomainFactory.createTransactionDTO();
        Account account = DomainFactory.createAccount();
        account.setAccountStatus(AccountStatus.LOCKED);
        when(accountRepo.findById(any(UUID.class))).thenReturn(Optional.ofNullable(account));
        assertThrows(AccountLockedException.class,() -> transactionService.executeTransfer(transactionDTO));
    }

    @Test
    void test_executeTransfer_BalanceException() {
        List<Account> listOfAccount = DomainFactory.createListOfAccount();
        Account fromAccount = listOfAccount.get(0);
        Account toAccount = listOfAccount.get(2);
        fromAccount.setBalance("0");
        TransactionDTO transactionDTO = DomainFactory.createTransactionDTO();
        FinancialTransaction financialTransaction = DomainFactory.createFinancialTransaction();
        when(accountRepo.findById(transactionDTO.getFromAccount())).thenReturn(Optional.of(fromAccount));
        when(accountRepo.findById(transactionDTO.getToAccount())).thenReturn(Optional.of(toAccount));
        assertThrows(NotEnoughBalanceException.class,() -> transactionService.executeTransfer(transactionDTO));
    }
    @Test
    void fetchTransHistoryById() {
        List<FinancialTransaction> financialTransactionList = DomainFactory.createListOfFinancialTransaction();
        TransactionDTO transactionDTO = DomainFactory.createTransactionDTO();
        when(transactionRepo.findByToAccountOrFromAccount(any(UUID.class),any(UUID.class))).thenReturn(financialTransactionList);
        when(transactionToTransactionDTOMapper.apply(any(FinancialTransaction.class))).thenReturn(transactionDTO);
        assertThat(transactionService.fetchTransHistoryById(UUID.randomUUID())).isNotNull();
        assertThat(transactionService.fetchTransHistoryById(UUID.randomUUID()).size()).isGreaterThan(0);
    }

    @Test
    void test_updateBalance_Success(){
        Double balance = Double.parseDouble("1000.345");
        String balanceCurrency= "EUR";
        Double trxAmount = Double.parseDouble("3456.67");
        String trxCurrency = "EUR";
        TrxType trxType = TrxType.CREDIT;
        Double updatedAmount = Double.sum(balance,trxAmount);
        assertThat(transactionService.updateBalance(balance,balanceCurrency,trxAmount,trxCurrency,trxType).getNumber().doubleValueExact()).isEqualTo(updatedAmount);
    }
}