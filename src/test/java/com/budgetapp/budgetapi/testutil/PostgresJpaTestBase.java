package com.budgetapp.budgetapi.testutil;

import com.budgetapp.budgetapi.model.enums.CreditOrDebit;
import com.budgetapp.budgetapi.model.transaction.BankAccountModel;
import com.budgetapp.budgetapi.model.transaction.BucketModel;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.service.dto.ActiveStatus;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;

public abstract class PostgresJpaTestBase {

    protected static final PostgreSQLContainer<?> postgres =
            SharedPostgresContainer.getInstance();
    @Autowired
    protected TestEntityManager em;

    @Autowired
    Flyway flyway;


    @BeforeEach
    void resetSchema() {
        flyway.clean();
        flyway.migrate();
    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.flyway.clean-disabled", () -> "false");
    }

    protected Users persistUser(String username) {
        Users u = new Users();
        u.setUsername(username);
        u.setPassword("test-password");
        return em.persistFlushFind(u);
    }

    protected BankAccountModel persistAccount(Users user, String name) {
        BankAccountModel a = BankAccountModel.builder()
                .user(user)
                .name(name)
                .creditOrDebit(CreditOrDebit.DEBIT)
                .balance(new BigDecimal("100.00"))
                .status(ActiveStatus.ACTIVE)
                .build();

        return em.persistFlushFind(a);
    }

    protected BucketModel persistBucket(Users user, String name, BankAccountModel bankAccountModel) {
        BucketModel b = BucketModel.builder()
                .name(name)
                .bankAccount(bankAccountModel)
                .user(user)
                .balance(new BigDecimal("0.00"))
                .build();

        return em.persistFlushFind(b);
    }
}
