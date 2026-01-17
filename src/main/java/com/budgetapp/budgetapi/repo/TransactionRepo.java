package com.budgetapp.budgetapi.repo;


import com.budgetapp.budgetapi.model.transaction.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionModel, Long> {

    List<TransactionModel> findByUserId(Long userId);


    TransactionModel findByIdAndUserId(Long id, Long userId);

    Iterable<TransactionModel> findByDateAndUserId(LocalDate date, Long userId);
}
