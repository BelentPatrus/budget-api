package com.budgetapp.budgetapi.service;

import com.budgetapp.budgetapi.model.enums.BudgetTargetType;
import com.budgetapp.budgetapi.model.transaction.BucketModel;
import com.budgetapp.budgetapi.model.transaction.BudgetPeriod;
import com.budgetapp.budgetapi.model.transaction.BudgetTargetModel;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.BudgetPeriodRepo;
import com.budgetapp.budgetapi.repo.BudgetTargetRepo;
import com.budgetapp.budgetapi.service.dto.BudgetDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetTargetModelService {
    private final BudgetTargetRepo repo;
    private final BudgetPeriodRepo periodRepo;
    private final BucketService bucketService;

    public BudgetTargetModelService(BudgetTargetRepo repo, BudgetPeriodRepo periodRepo, BucketService bucketService) {
        this.periodRepo = periodRepo;
        this.repo = repo;
        this.bucketService = bucketService;
    }

    public List<BudgetDTO> getBudgetsInPeriod(Users user, Long periodId) {
        return repo.findAllByUserIdAndPeriodId(user.getId(), periodId)
                .stream()
                .map(budget -> {
                    BudgetDTO dto = new BudgetDTO();
                    dto.setId(budget.getId());
                    dto.setName(budget.getName());
                    dto.setType(budget.getType());
                    dto.setBucketId(budget.getBucket().getId());
                    dto.setPlanned(budget.getPlanned());
                    return dto;

        }).toList();
    }

    public BudgetDTO addBudget(BudgetDTO budgetDTO, Users user) {
        BudgetPeriod period = periodRepo.findById(budgetDTO.getPeriodId()).orElse(null);
        if (period != null) {
            BudgetTargetModel.BudgetTargetModelBuilder budget = BudgetTargetModel
                    .builder()
                    .period(period)
                    .name(budgetDTO.getName())
                    .type(budgetDTO.getType())
                    .user(user);
            if(budgetDTO.getType() == BudgetTargetType.BUCKET){
                BucketModel bucket = bucketService.getBucketById(String.valueOf(budgetDTO.getBucketId()), user.getId());
                budget.bucket(bucket);
            }
            budget.planned(budgetDTO.getPlanned());
            repo.save(budget.build());

        }
        return budgetDTO;
    }
}
