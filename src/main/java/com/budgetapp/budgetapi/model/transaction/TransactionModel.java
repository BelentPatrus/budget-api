package com.budgetapp.budgetapi.model.transaction;

import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.model.enums.IncomeOrExpense;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class TransactionModel {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_transaction_user")
    )
    private Users user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="bank_account_id", nullable=false,
            foreignKey=@ForeignKey(name="fk_transaction_bank_account"))
    private BankAccountModel bankAccount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "bucket_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_transaction_bucket")
    )
    private BucketModel bucket;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncomeOrExpense incomeOrExpense;

}
