package com.budgetapp.budgetapi.service;


import com.budgetapp.budgetapi.model.transaction.TransactionModel;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepo repo;

    @Autowired
    public TransactionService(TransactionRepo repo) {
        this.repo = repo;
    }

    public List<TransactionModel> getTransactions(int userId) {
        return repo.findByUserId(userId);
    }

    public TransactionModel addTransaction(TransactionModel transaction) {
        return repo.save(transaction);
    }
}
