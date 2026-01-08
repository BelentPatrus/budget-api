package com.budgetapp.budgetapi.model.transaction;

import com.budgetapp.budgetapi.model.enums.CreditOrDebit;
import com.budgetapp.budgetapi.model.user.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(
        name = "bank_account_model",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_bank_account_user_name",
                columnNames = {"user_id", "name"}
        )
)
public class BankAccountModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_bank_account_user")
    )
    private Users user;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreditOrDebit creditOrDebit;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
}
