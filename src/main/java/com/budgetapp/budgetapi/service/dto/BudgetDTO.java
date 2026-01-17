package com.budgetapp.budgetapi.service.dto;

import com.budgetapp.budgetapi.model.enums.BudgetTargetType;
import com.budgetapp.budgetapi.model.transaction.BucketModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDTO {
    private Long periodId;
    private Long id;
    private String name;
    private BudgetTargetType type;
    private Long bucketId;
    private BigDecimal planned;

}
