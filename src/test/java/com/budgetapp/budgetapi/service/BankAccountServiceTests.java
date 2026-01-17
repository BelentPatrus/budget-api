package com.budgetapp.budgetapi.service;


import com.budgetapp.budgetapi.model.enums.CreditOrDebit;
import com.budgetapp.budgetapi.model.transaction.BankAccountModel;
import com.budgetapp.budgetapi.model.transaction.BucketModel;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.BankAccountRepo;
import com.budgetapp.budgetapi.repo.BucketRepo;
import com.budgetapp.budgetapi.service.dto.ActiveStatus;
import com.budgetapp.budgetapi.service.dto.BankAccountDTO;
import com.budgetapp.budgetapi.service.dto.BucketDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTests {

    @Mock BankAccountRepo bankAccountRepo;
    @Mock
    BucketRepo bucketRepo;

    @InjectMocks
    BankAccountService service;

    private Users u1;

    @BeforeEach
    void setup() {
        u1 = new Users(1L, "test", "test");
    }

    @Test
    void getBankAccount_delegatesToRepo() {
        when(bankAccountRepo.findByNameAndUserId("Chequing", 1L)).thenReturn(new BankAccountModel());

        BankAccountModel found = service.getBankAccount("Chequing", 1L);

        assertThat(found).isNotNull();
        verify(bankAccountRepo).findByNameAndUserId("Chequing", 1L);
    }

    @Test
    void getBankAccounts_mapsModelsToDtos() {
        BankAccountModel a = new BankAccountModel();
        a.setId(10L);
        a.setName("Chequing");
        a.setCreditOrDebit(CreditOrDebit.DEBIT);
        a.setBalance(new BigDecimal("123.45"));
        a.setStatus(ActiveStatus.ACTIVE);

        when(bankAccountRepo.findAllByUserId(1L)).thenReturn(List.of(a));

        List<BankAccountDTO> dtos = service.getBankAccounts(1L);

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getId()).isEqualTo(10L);
        assertThat(dtos.get(0).getName()).isEqualTo("Chequing");
        assertThat(dtos.get(0).getBalance()).isEqualByComparingTo("123.45");
        assertThat(dtos.get(0).getCreditOrDebit()).isEqualTo(CreditOrDebit.DEBIT);
        assertThat(dtos.get(0).getStatus()).isEqualTo(ActiveStatus.ACTIVE);
    }

    @Test
    void getBuckets_returnsBucketsInBankAccount_mapsModelsToDtos() {
        BankAccountModel a = new BankAccountModel();
        a.setId(10L);
        a.setName("Chequing");
        a.setCreditOrDebit(CreditOrDebit.DEBIT);
        a.setBalance(new BigDecimal("123.45"));
        a.setStatus(ActiveStatus.ACTIVE);
        a.setUser(u1);

        BucketModel b = BucketModel
                .builder()
                .bankAccount(a)
                .name("Groceries")
                .balance(new BigDecimal("123.45"))
                .id(1L)
                .user(a.getUser())
                .build();
        BucketModel c = BucketModel
                .builder()
                .bankAccount(a)
                .name("savings")
                .balance(new BigDecimal("100"))
                .id(2L)
                .user(a.getUser())
                .build();


        when(bucketRepo.findAllByBankAccountIdAndUserId(a.getId(), a.getUser().getId())).thenReturn(List.of(b, c));

        List<BucketDto> dtos = service.getBuckets(a.getId(), a.getUser().getId());

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(0).getName()).isEqualTo("Groceries");
        assertThat(dtos.get(0).getBalance()).isEqualByComparingTo("123.45");
        assertThat(dtos.get(1).getId()).isEqualTo(2L);
        assertThat(dtos.get(1).getName()).isEqualTo("savings");
        assertThat(dtos.get(1).getBalance()).isEqualByComparingTo("100");
        assertThat(dtos.get(0).getBankAccountId()).isEqualByComparingTo(a.getId());
        assertThat(dtos.get(1).getBankAccountId()).isEqualByComparingTo(a.getId());

    }


}