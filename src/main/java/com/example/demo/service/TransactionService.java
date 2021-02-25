package com.example.demo.service;

import com.example.demo.commons.AccountStatus;
import com.example.demo.commons.MoneyValue;
import com.example.demo.commons.TransactionType;
import com.example.demo.commons.TrxType;
import com.example.demo.domainmapper.TransactionDTOToTransactionMapper;
import com.example.demo.domainmapper.TransactionToTransactionDTOMapper;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.exception.AccountLockedException;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.NotEnoughBalanceException;
import com.example.demo.domain.Account;
import com.example.demo.domain.FinancialTransaction;
import com.example.demo.repo.AccountRepo;
import com.example.demo.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionDTOToTransactionMapper transactionDTOToTransactionMApper;
    private final TransactionToTransactionDTOMapper transactionToTransactionDTOMapper;
    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;

    public TransactionDTO executeDeposit(TransactionDTO transactionDTO) {
        Account account = accountRepo.findById(transactionDTO.getToAccount())
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account Not Found for ID %s",transactionDTO.getToAccount())));
        if(isAccountLocked(account))
            throw  new AccountLockedException(String.format("Account Locked For Account Id %s",transactionDTO.getToAccount()));
        Double accountBalance =Double.parseDouble(account.getBalance());
        String accountCurrency = account.getCurrency();
        Double trxAmnt =  Double.parseDouble(transactionDTO.getAmount().getAmount());
        String trxCurrecy =  transactionDTO.getAmount().getCurrency();
        MonetaryAmount updatedBalance = updateBalance(accountBalance,accountCurrency,trxAmnt,trxCurrecy,TrxType.CREDIT);
        account.setBalance(updatedBalance.getNumber().toString());
        accountRepo.save(account);
        FinancialTransaction financialTransaction = transactionDTOToTransactionMApper.apply(transactionDTO);
        TransactionDTO depositTRansaction =  transactionToTransactionDTOMapper.apply(transactionRepo.save(financialTransaction));
        return depositTRansaction;
    }

    public TransactionDTO executeTransfer(TransactionDTO transactionDTO) {
        UUID fromAccountID = transactionDTO.getFromAccount();
        UUID toAcccountID = transactionDTO.getToAccount();
        Account fromAccount  = accountRepo.findById(fromAccountID)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account Not Found for ID %s",fromAccountID)));
        Account toAccount = accountRepo.findById(toAcccountID)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account Not Found for ID %s",toAcccountID)));
        if(isAccountLocked(fromAccount) || isAccountLocked(toAccount))
            throw  new AccountLockedException(String.format("Account Locked For Account Id %s",(fromAccountID!=null?fromAccountID:toAcccountID)));
        final Double debitAccountBalance = Double.parseDouble(fromAccount.getBalance());
        final String debitCurrency = fromAccount.getCurrency();
        final Double creditAccountBalance = Double.parseDouble(fromAccount.getBalance());
        final String creditCurrency = fromAccount.getCurrency();
        final Double trxAmount =   Double.parseDouble(transactionDTO.getAmount().getAmount());
        final String trxCurrency = transactionDTO.getAmount().getCurrency();
        boolean balanceCheck = isBalanceAvailable(debitAccountBalance, debitCurrency,
                Double.parseDouble(transactionDTO.getAmount().getAmount()),transactionDTO.getAmount().getCurrency());
        if(!balanceCheck)
            throw new NotEnoughBalanceException(String.format("NotEnoughBalanceException in  Account %s",fromAccountID));
        MonetaryAmount  toAccoutBalance = updateBalance(creditAccountBalance,creditCurrency,trxAmount,trxCurrency,TrxType.CREDIT);
        MonetaryAmount fromAccountBalance = updateBalance(debitAccountBalance, debitCurrency,
                trxAmount,trxCurrency,TrxType.DEBIT);
        fromAccount.setBalance(fromAccountBalance.getNumber().toString());
        toAccount.setBalance(toAccoutBalance.getNumber().toString());
        accountRepo.save(fromAccount);
        accountRepo.save(toAccount);
        FinancialTransaction financialTransaction = transactionRepo.save(transactionDTOToTransactionMApper.apply(transactionDTO));
        return transactionToTransactionDTOMapper.apply(financialTransaction);
    }


    public List<TransactionDTO> fetchTransHistoryById(UUID accountID) {
        List<FinancialTransaction> financialTransactions =
                transactionRepo.findByToAccountOrFromAccount(accountID,accountID);
        return   financialTransactions.stream()
                .map(financialTransaction -> transactionToTransactionDTOMapper.apply(financialTransaction)).collect(Collectors.toList());
    }

    public boolean isAccountLocked(Account fromAccount) {
        return fromAccount.getAccountStatus().equals(AccountStatus.LOCKED);
    }


    public MonetaryAmount updateBalance(Double balance, String balanceCurrency, Double trxAmount, String trxCurrency, TrxType trxType) {
        MonetaryAmount currentBalance = Monetary.getDefaultAmountFactory()
                .setNumber(balance).setCurrency(balanceCurrency).create();
        MonetaryAmount trxMonetaryAmt = Monetary.getDefaultAmountFactory()
                .setNumber(trxAmount)
                .setCurrency(trxCurrency).create();
        MonetaryAmount updatedBalance = trxType.equals(TrxType.CREDIT)?
                currentBalance.add(trxMonetaryAmt):currentBalance.subtract(trxMonetaryAmt);
        return  updatedBalance;
    }

    public boolean isBalanceAvailable(Double balanceAmount,String balanceCurrency,Double trxAmount,String currency){

        MonetaryAmount fromAccountBalance = Monetary.getDefaultAmountFactory()
                .setNumber(balanceAmount)
                .setCurrency(balanceCurrency).create();

        MonetaryAmount tranSactionAmount = Monetary.getDefaultAmountFactory()
                .setNumber(trxAmount)
                .setCurrency(currency).create();

        return fromAccountBalance.isLessThan(tranSactionAmount)?false:true;

    }

    /**
     * Developer Helper class
     * @return
     */
    public TransactionDTO getTrans() {
        return initData();
    }


    private TransactionDTO initData(){
        TransactionDTO transactionDTO = new TransactionDTO();
        MoneyValue moneyValue = MoneyValue.builder().amount("1000.345").currency("EUR").build();
        transactionDTO.setAmount(moneyValue);
        transactionDTO.setToAccount(UUID.randomUUID());
        transactionDTO.setTransactionType(TransactionType.DEPOSIT);
        transactionDTO.setTransactionId(UUID.randomUUID());
        return transactionDTO;
    }

}
