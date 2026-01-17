package com.budgetapp.budgetapi.repository;



import com.budgetapp.budgetapi.model.enums.CreditOrDebit;
import com.budgetapp.budgetapi.model.transaction.BankAccountModel;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.BankAccountRepo;
import com.budgetapp.budgetapi.service.dto.ActiveStatus;
import com.budgetapp.budgetapi.testutil.PostgresJpaTestBase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class BankAccountRepoTests extends PostgresJpaTestBase {

    @Autowired
    private BankAccountRepo repo;

    @Test
    void findByNameAndUserId_returnsMatch() {
        Users u1 = persistUser("a@test.com");
        BankAccountModel acc = persistAccount(u1, "Chequing");

        BankAccountModel found = repo.findByNameAndUserId("Chequing", u1.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId())
                .as("bankAccount.id")
                .isEqualTo(acc.getId());
    }

    @Test
    void findAllByUserId_returnsOnlyThatUsersAccounts() {
        Users u1 = persistUser("a@test.com");
        Users u2 = persistUser("b@test.com");

        persistAccount(u1, "Chequing");
        persistAccount(u1, "Savings");
        persistAccount(u2, "Chequing"); // same name allowed for different user

        List<BankAccountModel> u1Accounts = repo.findAllByUserId(u1.getId());

        assertThat(u1Accounts).hasSize(2);
        assertThat(u1Accounts).allMatch(a -> a.getUser().getId().equals(u1.getId()));
    }

    @Test
    void findByIdAndUserId_enforcesIsolation() {
        Users u1 = persistUser("a@test.com");
        Users u2 = persistUser("b@test.com");

        BankAccountModel accU1 = persistAccount(u1, "Chequing");

        assertThat(repo.findByIdAndUserId(accU1.getId(), u1.getId())).isNotNull();
        assertThat(repo.findByIdAndUserId(accU1.getId(), u2.getId())).isNull();
    }

    @Test
    void uniqueConstraint_userIdPlusName_isEnforced() {
        Users u1 = persistUser("a@test.com");

        persistAccount(u1, "Chequing");

        BankAccountModel dup = BankAccountModel.builder()
                .user(u1)
                .name("Chequing") // same name same user -> should fail
                .creditOrDebit(CreditOrDebit.DEBIT)
                .balance(new BigDecimal("50.00"))
                .status(ActiveStatus.ACTIVE)
                .build();

        // save + flush to force DB constraint check now
        assertThatThrownBy(() -> repo.saveAndFlush(dup))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}