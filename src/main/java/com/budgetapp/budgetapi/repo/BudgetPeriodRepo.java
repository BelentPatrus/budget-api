package com.budgetapp.budgetapi.repo;

import com.budgetapp.budgetapi.model.transaction.BudgetPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetPeriodRepo extends JpaRepository<BudgetPeriod, Long> {
}
