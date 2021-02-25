package com.example.demo.domainmapper;

import com.example.demo.commons.MoneyValue;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.domain.FinancialTransaction;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TransactionToTransactionDTOMapper implements Function<FinancialTransaction, TransactionDTO> {

    @Override
    public TransactionDTO apply(FinancialTransaction financialTransaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(financialTransaction.getUuid());
        transactionDTO.setTransactionType(financialTransaction.getTransactionType());
        transactionDTO.setFromAccount(financialTransaction.getFromAccount());
        transactionDTO.setToAccount(financialTransaction.getToAccount());
        transactionDTO.setAmount(MoneyValue.builder().amount(financialTransaction.getAmount())
                .currency(financialTransaction.getCurrency()).build());

        return transactionDTO;
    }
}
