package com.budgetapp.budgetapi.model.transaction;

import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.model.enums.TransactionType;
import com.budgetapp.budgetapi.service.dto.TransactionDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "transaction_model",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tx_user_date_amount_desc",
                        columnNames = {
                                "user_id",
                                "date",
                                "amount",
                                "description"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_tx_user_date",
                        columnList = "user_id,date"
                )
        }
)
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
    private TransactionType transactionType;

    @Column(name = "transfer_group_id")
    private UUID transferGroupId;



    public TransactionModel(TransactionDto transactionDto, Users user) {
        this.user = user;
        this.description = transactionDto.getDescription();
        this.amount = transactionDto.getAmount();
        this.date = LocalDate.parse(transactionDto.getDate());
        this.transactionType = transactionDto.getTransactionType();
    }

}
