package com.example.demo.domainmapper;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.domain.FinancialTransaction;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.function.Function;

@Component
public class TransactionDTOToTransactionMapper implements Function<TransactionDTO, FinancialTransaction> {

    @Override
    public FinancialTransaction apply(TransactionDTO transactionDTO) {
        FinancialTransaction financialTransaction = new FinancialTransaction();
        financialTransaction.setToAccount(transactionDTO.getToAccount());
        financialTransaction.setFromAccount(transactionDTO.getFromAccount());
        financialTransaction.setAmount(transactionDTO.getAmount().getAmount());
        financialTransaction.setCurrency(transactionDTO.getAmount().getCurrency());
        financialTransaction.setExecutedDate(LocalDateTime.now());
        financialTransaction.setTransactionType(transactionDTO.getTransactionType());
        return financialTransaction;
    }
}
