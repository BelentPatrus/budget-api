package com.budgetapp.budgetapi.repo;

import com.budgetapp.budgetapi.model.transaction.BucketModel;
import com.budgetapp.budgetapi.model.transaction.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface BucketRepo extends JpaRepository<BucketModel, Long> {

    BucketModel findByNameAndUserId(String name, Integer userId);


    List<BucketModel> findAllByUserId(Integer userId);

    List<BucketModel> findAllByBankAccountIdAndUserId(Long accountId, Integer userId);
}
