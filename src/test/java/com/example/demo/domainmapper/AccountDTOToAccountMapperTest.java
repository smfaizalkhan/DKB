package com.example.demo.domainmapper;

import com.example.demo.domain.Account;
import com.example.demo.dto.AccountDTO;
import com.example.demo.factory.DomainFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccountDTOToAccountMapperTest {

    @InjectMocks
    private AccountDTOToAccountMapper accountDTOToAccountMapper;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void apply() {
        AccountDTO accountDTO = DomainFactory.createAccountDTO();
        assertThat(accountDTOToAccountMapper.apply(accountDTO)).isInstanceOf(Account.class);

    }
}