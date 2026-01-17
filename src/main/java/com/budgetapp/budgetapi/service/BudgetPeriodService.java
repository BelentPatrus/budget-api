package com.budgetapp.budgetapi.service;

import com.budgetapp.budgetapi.model.transaction.BudgetPeriod;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.BudgetPeriodRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetPeriodService {

    private final BudgetPeriodRepo repo;

    public BudgetPeriodService(BudgetPeriodRepo repo){
        this.repo = repo;
    }


    public List<BudgetPeriod> getPeriods(Users user) {
        return repo.findAll();

    }
}
