package com.budgetapp.budgetapi.repo;

import com.budgetapp.budgetapi.model.transaction.BudgetPeriod;
import com.budgetapp.budgetapi.model.transaction.BudgetTargetModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetTargetRepo extends JpaRepository<BudgetTargetModel, Long> {

    List<BudgetTargetModel> findAllByUserIdAndPeriodId(Long userId, Long periodId);
}
