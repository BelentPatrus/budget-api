package com.budgetapp.budgetapi.service;

import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.BudgetTargetRepo;
import com.budgetapp.budgetapi.service.dto.BudgetDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetTargetModelService {
    private final BudgetTargetRepo repo;

    public BudgetTargetModelService(BudgetTargetRepo repo){
        this.repo = repo;
    }

    public List<BudgetDTO> getBudgetsInPeriod(Users user, Long periodId) {
        return repo.findAllByUserIdAndPeriodId(user.getId(), periodId)
                .stream()
                .map(budget -> {
                    BudgetDTO dto = new BudgetDTO();
                    dto.setId(budget.getId());
                    dto.setName(budget.getName());
                    dto.setType(budget.getType().toString());
                    dto.setBucketName(budget.getBucket().getName());
                    dto.setPlanned(budget.getPlanned());
                    return dto;

        }).toList();
    }
}
