package com.budgetapp.budgetapi.repo;

import com.budgetapp.budgetapi.model.transaction.BankAccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepo extends JpaRepository<BankAccountModel, Long> {

    BankAccountModel findByNameAndUserId(String name, Long userId);

    List<BankAccountModel> findAllByUserId(Long userId);

    BankAccountModel findByIdAndUserId(Long bankAccountId, Long id);
}
