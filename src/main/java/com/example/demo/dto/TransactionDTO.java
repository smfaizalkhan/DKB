package com.example.demo.dto;

import com.example.demo.commons.MoneyValue;
import com.example.demo.commons.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {



    private UUID transactionId;
    @NotNull
    private TransactionType transactionType;
    private UUID fromAccount;
    @NotNull(message = "Cannot be Empty")
    private UUID toAccount;
    @NotNull
    private MoneyValue amount;
}
