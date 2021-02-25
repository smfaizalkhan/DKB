package com.example.demo.controller;


import com.example.demo.commons.TransactionType;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.factory.DomainFactory;
import com.example.demo.service.TransactionService;
import com.example.demo.util.TestUtil;
import com.example.demo.validator.TransactionValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.example.demo.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private TransactionValidator transactionValidator;

    @Test
    void test_deposit() throws Exception {
        TransactionDTO transactionDTO = DomainFactory.createTransactionDTO();
        when(transactionService.executeDeposit(any(TransactionDTO.class))).thenReturn(transactionDTO);
        TransactionDTO requestTransactionDTO = DomainFactory.createTransactionDTO();
        requestTransactionDTO.setTransactionType(TransactionType.DEPOSIT);
        mockMvc.perform(post("/dkb/deposit")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestTransactionDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").exists())
                .andExpect(jsonPath("$.transactionType").value(TransactionType.DEPOSIT.name()));
    }

    @Test
    void test_transfer() throws Exception {
        TransactionDTO transactionDTO = DomainFactory.createTransactionDTO();
        when(transactionService.executeTransfer(any(TransactionDTO.class))).thenReturn(transactionDTO);
        TransactionDTO requestTransactionDTO = DomainFactory.createTransactionDTO();
        requestTransactionDTO.setTransactionType(TransactionType.TRANSFER);
        mockMvc.perform(post("/dkb/transfer").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestTransactionDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").exists());

    }


    @Test
    void fetchTransactionHistory() throws Exception {
        List<TransactionDTO> transactionDTO = Collections.singletonList(DomainFactory.createTransactionDTO());
        when(transactionService.fetchTransHistoryById(any(UUID.class))).thenReturn(transactionDTO);
        UUID accountID = transactionDTO.get(0).getFromAccount();
        mockMvc.perform(get("/dkb/"+accountID+"/trans-history")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").exists());
    }
}