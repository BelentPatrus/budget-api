package com.budgetapp.budgetapi.repository;


import com.budgetapp.budgetapi.model.enums.CreditOrDebit;
import com.budgetapp.budgetapi.model.transaction.BankAccountModel;
import com.budgetapp.budgetapi.model.transaction.BucketModel;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.BucketRepo;
import com.budgetapp.budgetapi.service.dto.ActiveStatus;
import com.budgetapp.budgetapi.testutil.PostgresJpaTestBase;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

@DataJpaTest
public class BucketRepoTests extends PostgresJpaTestBase {

    @Autowired
    private BucketRepo repo;

    private Users u1;
    private BucketModel b1;
    private BankAccountModel u1Acc;

    private Users u2;
    private BucketModel b2;
    private BankAccountModel u2Acc;

    @BeforeEach
    void setup(){
        u1 = persistUser("u1");
        u2 = persistUser("u2");

        u1Acc = persistAccount(u1, "Chequing");
        u2Acc = persistAccount(u2, "Chequing");

        b1 = persistBucket(u1, "b1", u1Acc);
        b2 = persistBucket(u2, "b2", u2Acc);
    }



    @Test
    void findByNameAndUserId_returnsMatch() {

        //act
        BucketModel found = repo.findByNameAndUserId("b1", u1.getId());
        BucketModel found2 = repo.findByNameAndUserId("b2", u1.getId());

        //assert
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(b1.getId());
        assertThat(found2).isNull();
    }

    @Test
    void findAllByUserId_returnsOnlyThatUsersBuckets() {
        BucketModel b3 = persistBucket(u1, "b3", u1Acc);

        //act
        Iterable<BucketModel> found = repo.findAllByUserId(u1.getId());

        //assert
        assertThat(found).hasSize(2);
        assertThat(found).allMatch(b -> b.getUser().getId().equals(u1.getId()));
    }

    @Test
    void findByIdAndUserId_enforcesIsolation() {

        assertThat(repo.findByIdAndUserId(b1.getId(), u1.getId())).isNotNull();
        assertThat(repo.findByIdAndUserId(b1.getId(), u2.getId())).isNull();
    }

    @Test
    void findAllByBankAccountIdAndUserId_returnsOnlyThatUsersBuckets() {

        BucketModel b3 = persistBucket(u1, "b3", u1Acc);

        //act
        Iterable<BucketModel> found = repo.findAllByBankAccountIdAndUserId(b1.getBankAccount().getId(), u1.getId());

        //assert
        assertThat(found).hasSize(2);
        assertThat(found).allMatch(b -> b.getUser().getId().equals(u1.getId()));
    }

    @Test
    void uniqueConstraint_namePerUser() {
        assertThatThrownBy(() -> persistBucket(u1, "b1", u1Acc)).isInstanceOf(Exception.class);
    }


}
