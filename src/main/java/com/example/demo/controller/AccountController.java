package com.example.demo.controller;

import com.example.demo.commons.AccountType;
import com.example.demo.commons.MoneyValue;
import com.example.demo.dto.AccountDTO;
import com.example.demo.error.ApiError;
import com.example.demo.service.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dkb")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/openAccount")
    @ApiOperation(value = "Account opening")
    @ApiResponses(value = {
            @ApiResponse(code = 200 ,message = "Opening an Account" ,response = AccountDTO.class),
            @ApiResponse(code = 400 ,message = "Bad Request" ,response = ApiError.class),
            @ApiResponse(code = 404 ,message = "Not Found" ,response = ApiError.class),
            @ApiResponse(code = 500 ,message = "Internal Server" ,response = ApiError.class)

    })
    public AccountDTO openAccount(@RequestBody AccountDTO accountDTO){
    return accountService.createAccount(accountDTO);
    }

    @GetMapping("/{account_id}/unlock")
    public AccountDTO unlockAccount(@PathVariable(name = "account_id") UUID accountId){
        return accountService.unLockAccount(accountId);
    }


    @GetMapping("/{account_id}/balance")
    public MoneyValue fetchBalance(@PathVariable(name = "account_id") UUID accountId){
        return accountService.fetchBalance(accountId);
    }

    @GetMapping("/{account_type}/accounts")
    public List<AccountDTO> fetchAccounts(@PathVariable(name = "account_type") AccountType accountType){
        return accountService.fetchAccountsByType(accountType);
    }

    /**
     *
     * Developer methods
     */

    @GetMapping("/{account_id}/acccount")
    public AccountDTO getAccount(@PathVariable(name = "account_id") UUID accountId){
        return accountService.getAccount(accountId);
    }

    @GetMapping("/acccounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }


}
