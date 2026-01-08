package com.budgetapp.budgetapi.repo;

import com.budgetapp.budgetapi.model.transaction.BankAccountModel;
import com.budgetapp.budgetapi.model.transaction.BucketModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepo extends JpaRepository<BankAccountModel, Long> {

    BankAccountModel findByNameAndUserId(String name, Integer userId);

    List<BankAccountModel> findAllByUserId(Integer userId);

    BankAccountModel findByIdAndUserId(long bankAccountId, Integer id);
}
