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

class AccountToAccountDTOMapperTest {

    @InjectMocks
    private AccountToAccountDTOMapper accountToAccountDTOMapper;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void apply() {
        Account account = DomainFactory.createAccount();
        assertThat(accountToAccountDTOMapper.apply(account)).isInstanceOf(AccountDTO.class);
    }
}