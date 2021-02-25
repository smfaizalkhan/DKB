package com.example.demo.validator;

import com.example.demo.commons.AccountType;
import com.example.demo.commons.TransactionType;
import com.example.demo.dto.AccountDTO;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionValidator implements Validator {

    private final AccountService accountService;

    @Override
    public boolean supports(Class<?> aClass) {
        return TransactionDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransactionDTO transactionDTO = (TransactionDTO) target;
        final UUID fromAccount = transactionDTO.getFromAccount();
        final UUID toAccount = transactionDTO.getToAccount();
        if(transactionDTO.getTransactionType().equals(TransactionType.TRANSFER))
            validateTransferTransaction(errors, transactionDTO, fromAccount, toAccount);

    }

    private void validateTransferTransaction(Errors errors, TransactionDTO transactionDTO, UUID fromAccount, UUID toAccount) {
        if(Objects.isNull(fromAccount)){
            errors.rejectValue("toAccount","fromAccount needed for Transaction Type Transfer");
        }

        AccountDTO fromAccountDTO = accountService.getAccount(fromAccount);
        AccountDTO toAccountDTO = accountService.getAccount(toAccount);
        AccountType fromAccountType = fromAccountDTO.getAccountType();
        AccountType toAccountType = toAccountDTO.getAccountType();

        if(fromAccountType.equals(AccountType.SAVINGS_ACCOUNT) && !toAccountType.equals(AccountType.CHECKING_ACCOUNT)){
            errors.rejectValue("fromAccount","Cannot transfer from  to");
        }
        if(fromAccountType.equals(AccountType.PRIVATE_LOAN_ACCOUNT)){
            System.out.println("PRovate Loan accont not posdsfisndfser");
            errors.rejectValue("fromAccount","Cannot trnsafer from ");
        }
    }
}
