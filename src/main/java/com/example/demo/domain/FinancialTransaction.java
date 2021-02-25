package com.example.demo.domain;

import com.example.demo.commons.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialTransaction {

    @Id
    @GeneratedValue
    private UUID uuid;
    private String amount;
    private String currency;
    private UUID fromAccount;
    private UUID toAccount;
    private LocalDateTime executedDate;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}
