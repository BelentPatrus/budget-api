package com.budgetapp.budgetapi.model.transaction;

import com.budgetapp.budgetapi.model.enums.BudgetTargetType;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.service.dto.BudgetDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "budget_target_model",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_budget_target_user_type_name",
                columnNames = {"user_id", "type", "name"}
        )
)
@Getter
@Setter
public class BudgetTargetModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false,
            foreignKey=@ForeignKey(name="fk_budget_target_user"))
    private Users user;

    @ManyToOne(optional = false)
    @JoinColumn(name="budget_period_id", nullable=false,
            foreignKey=@ForeignKey(name="fk_budget_period"))
    private BudgetPeriod period;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BudgetTargetType type;

    //only used when type is BUCKET
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bucket_model_id", foreignKey=@ForeignKey(name="fk_budget_target_fund"))
    private BucketModel bucket;

    @Column(name = "planned", nullable = false)
    private BigDecimal planned;




}
