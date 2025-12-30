package com.budgetapp.budgetapi.model.transaction;

import com.budgetapp.budgetapi.model.user.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

}
