package com.budgetapp.budgetapi.repo;


import com.budgetapp.budgetapi.model.transaction.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.http.HttpResponse;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionModel, Long> {

    List<TransactionModel> findByUserId(Integer userId);


    TransactionModel findByIdAndUserId(Long id, int userId);
}
