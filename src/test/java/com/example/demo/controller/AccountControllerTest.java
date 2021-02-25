package com.example.demo.controller;

import com.example.demo.commons.AccountStatus;
import com.example.demo.commons.AccountType;
import com.example.demo.dto.AccountDTO;
import com.example.demo.factory.DomainFactory;
import com.example.demo.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


import static com.example.demo.util.TestUtil.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {


    @MockBean
    private AccountService accountService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_openAccount() throws Exception {
        AccountDTO accountDTO = DomainFactory.createAccountDTO();
        when(accountService.createAccount(any(AccountDTO.class))).thenReturn(accountDTO);
        AccountDTO requestAccountDto = DomainFactory.createAccountDTO();
        requestAccountDto.setAccountStatus(AccountStatus.LOCKED);
        mockMvc.perform(post("/dkb/openAccount")
                .content(asJsonString(requestAccountDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.iban").exists())
                .andExpect(jsonPath("$.accountType").exists())
                .andExpect(jsonPath("$.accountStatus").exists());

    }

    @Test
    void test_unlockAccount() throws Exception {
        AccountDTO accountDTO = DomainFactory.createAccountDTO();
        when(accountService.unLockAccount(any(UUID.class))).thenReturn(accountDTO);
        mockMvc.perform(get("/dkb/" + accountDTO.getIban() + "/unlock")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.accountStatus").value(AccountStatus.NOT_LOCKED.name()));

    }

    @Test
    void test_fetchBalance() throws Exception {
        AccountDTO accountDTO = DomainFactory.createAccountDTO();
        when(accountService.fetchBalance(any(UUID.class))).thenReturn(accountDTO.getMoneyValue());
        mockMvc.perform(get("/dkb/" + accountDTO.getIban() + "/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(accountDTO.getMoneyValue().getAmount()));

    }

    @Test
    void  test_fetchAccounts() throws Exception {
        List<AccountDTO> accountDTOList = Collections.singletonList(DomainFactory.createAccountDTO());
        when(accountService.fetchAccountsByType(any(AccountType.class))).thenReturn(accountDTOList);
        AccountType requestAccountType = accountDTOList.get(0).getAccountType();
        mockMvc.perform(get("/dkb/" +requestAccountType+ "/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountType").value(requestAccountType.toString()));
    }
}