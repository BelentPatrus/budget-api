package com.budgetapp.budgetapi.controller;


import com.budgetapp.budgetapi.model.transaction.BudgetPeriod;
import com.budgetapp.budgetapi.model.transaction.BudgetTargetModel;
import com.budgetapp.budgetapi.model.user.UserPrincipal;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.service.BudgetPeriodService;
import com.budgetapp.budgetapi.service.BudgetTargetModelService;
import com.budgetapp.budgetapi.service.dto.BucketDto;
import com.budgetapp.budgetapi.service.dto.BudgetDTO;
import com.budgetapp.budgetapi.util.UserVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BudgetController {

    private final BudgetPeriodService budgetPeriodService;
    private final BudgetTargetModelService budgetTargetModelService;
    private final UserVerify userVerify;

    @Autowired
    public BudgetController(BudgetPeriodService budgetPeriodService,BudgetTargetModelService budgetTargetModelService, UserVerify userVerify ){
        this.budgetPeriodService = budgetPeriodService;
        this.budgetTargetModelService = budgetTargetModelService;
        this.userVerify = userVerify;
    }

    @GetMapping("/budgets/{periodId}")
    public List<BudgetDTO> getBudgets(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long periodId){
        Users user = userVerify.verifyUser(principal);
        return budgetTargetModelService.getBudgetsInPeriod(user, periodId);

    }

    @GetMapping("/budget-periods")
    public List<BudgetPeriod> getPeriods(@AuthenticationPrincipal UserPrincipal principal){
        Users user = userVerify.verifyUser(principal);
        return budgetPeriodService.getPeriods(user);
    }

    @PostMapping("/budget")
    public BudgetDTO addBudget(@AuthenticationPrincipal UserPrincipal principal, @RequestBody BudgetDTO budgetDTO){
        Users user = userVerify.verifyUser(principal);
        return budgetTargetModelService.addBudget(budgetDTO, user);


    }

}
