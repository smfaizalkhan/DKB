package com.example.demo.controller;

import com.example.demo.commons.MoneyValue;
import com.example.demo.commons.TransactionType;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.service.TransactionService;
import com.example.demo.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dkb")
@RequiredArgsConstructor
@Validated
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionValidator transactionValidator;


    @PostMapping("/deposit")
    public TransactionDTO deposit(@Valid @RequestBody TransactionDTO transactionDTO){
        if(!transactionDTO.getTransactionType().equals(TransactionType.DEPOSIT))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return  transactionService.executeDeposit(transactionDTO);
    }

    @PostMapping("/transfer")
    public TransactionDTO transfer(@Valid @RequestBody TransactionDTO transactionDTO,BindingResult bindingResult){
        transactionValidator.validate(transactionDTO,bindingResult);
        if(bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return    transactionService.executeTransfer(transactionDTO);
    }


    @GetMapping("/{account_id}/trans-history")
    public List<TransactionDTO> fetchTransactionHistory(@PathVariable(name = "account_id") UUID accountID){
        return transactionService.fetchTransHistoryById(accountID);
    }

}
