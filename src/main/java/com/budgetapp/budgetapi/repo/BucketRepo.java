package com.budgetapp.budgetapi.repo;

import com.budgetapp.budgetapi.model.transaction.BucketModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BucketRepo extends JpaRepository<BucketModel, Long> {

    BucketModel findByNameAndUserId(String name, Long userId);


    List<BucketModel> findAllByUserId(Long userId);

    List<BucketModel> findAllByBankAccountIdAndUserId(Long accountId, Long userId);

    BucketModel findByIdAndUserId(Long l, Long id);
}
