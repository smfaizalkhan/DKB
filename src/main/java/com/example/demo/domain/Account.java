package com.example.demo.domain;

import com.example.demo.commons.AccountType;
import com.example.demo.commons.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

   @Id
   @GeneratedValue
    private UUID iban;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private String balance;
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
}
