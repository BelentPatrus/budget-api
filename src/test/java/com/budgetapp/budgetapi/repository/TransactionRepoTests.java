package com.budgetapp.budgetapi.repository;

import com.budgetapp.budgetapi.model.enums.CreditOrDebit;
import com.budgetapp.budgetapi.model.enums.TransactionType;
import com.budgetapp.budgetapi.model.transaction.BankAccountModel;
import com.budgetapp.budgetapi.model.transaction.BucketModel;
import com.budgetapp.budgetapi.model.transaction.TransactionModel;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.BucketRepo;
import com.budgetapp.budgetapi.repo.TransactionRepo;
import com.budgetapp.budgetapi.testutil.PostgresJpaTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class TransactionRepoTests extends PostgresJpaTestBase {
    @Autowired
    private TransactionRepo repo;

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


    private TransactionModel persistTransaction(Users user, BucketModel bucket, BankAccountModel bankAccount, String description, BigDecimal amount, LocalDate date, TransactionType transactionType) {
        TransactionModel transaction = TransactionModel.builder()
                .user(user)
                .bucket(bucket)
                .bankAccount(bankAccount)
                .description(description)
                .amount(amount)
                .date(date)
                .transactionType(transactionType)
                .build();
        return repo.save(transaction);
    }



    @Test
    void findByUserId_returnsOnlyThatUsersTransactions() {
        TransactionModel t1 = persistTransaction(u1, b1, u1Acc, "t1", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.EXPENSE);
        TransactionModel t2 = persistTransaction(u2, b2, u2Acc, "t2", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.INCOME);
        TransactionModel t3 = persistTransaction(u1, b1, u1Acc, "t3", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.TRANSFER);

        //act
        Iterable<TransactionModel> found = repo.findByUserId(u1.getId());

        //assert
        assertThat(found).hasSize(2);
        assertThat(found).allMatch(t -> t.getUser().getId().equals(u1.getId()));

    }

    @Test
    void findByIdAndUserId_enforcesIsolation() {

        TransactionModel t1 = persistTransaction(u1, b1, u1Acc, "t1", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.EXPENSE);
        TransactionModel t2 = persistTransaction(u2, b2, u2Acc, "t2", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.INCOME);
        TransactionModel t3 = persistTransaction(u1, b1, u1Acc, "t3", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.TRANSFER);

        //act
        TransactionModel found = repo.findByIdAndUserId(t1.getId(), u1.getId());
        TransactionModel found2 = repo.findByIdAndUserId(t2.getId(), u1.getId());

        //assert
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(t1.getId());
        assertThat(found2).isNull();
    }

    @Test
    void findByDate_returnsOnlyThatUsersTransactions() {
        TransactionModel t1 = persistTransaction(u1, b1, u1Acc, "t1", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.EXPENSE);
        TransactionModel t2 = persistTransaction(u2, b2, u2Acc, "t2", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.INCOME);
        TransactionModel t3 = persistTransaction(u1, b1, u1Acc, "t3", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.TRANSFER);

        //act
        Iterable<TransactionModel> found = repo.findByDateAndUserId(LocalDate.now(), u1.getId());

        //assert
        assertThat(found).hasSize(2);
        assertThat(found).allMatch(t -> t.getUser().getId().equals(u1.getId()));
    }

    @Test
    void UniqueConstraint_enforced() {
        TransactionModel t1 = persistTransaction(u1, b1, u1Acc, "t1", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.EXPENSE);
        TransactionModel t2 = persistTransaction(u2, b2, u2Acc, "t2", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.INCOME);
        TransactionModel t3 = persistTransaction(u2, b2, u2Acc, "t2 copy", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.INCOME);

        assertThatThrownBy(() -> persistTransaction(u1, b1, u1Acc, "t1", BigDecimal.valueOf(100), LocalDate.now(), TransactionType.EXPENSE)).isInstanceOf(Exception.class);
        //act
    }


}
