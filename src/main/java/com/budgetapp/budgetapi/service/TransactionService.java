package com.budgetapp.budgetapi.service;


import com.budgetapp.budgetapi.model.enums.IncomeOrExpense;
import com.budgetapp.budgetapi.model.transaction.TransactionModel;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.TransactionRepo;
import com.budgetapp.budgetapi.service.dto.CreateTransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.time.LocalDate;
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

    public TransactionModel addTransaction(CreateTransactionDto transactionDto, Users user) {
        TransactionModel transaction = new TransactionModel();
        transaction.setUser(user);
        transaction.setDescription(transactionDto.getDescription());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDate(LocalDate.parse(transactionDto.getDate()));
        transaction.setIncomeOrExpense(transactionDto.getIncomeOrExpense().equalsIgnoreCase("INCOME") ? IncomeOrExpense.INCOME : IncomeOrExpense.EXPENSE);
        //need to add bank account and bucket
        return  repo.save(transaction);

    }

    public void deleteTransactions(int userId, Long id ) {
        TransactionModel transaction = repo.findByIdAndUserId(id,userId);
        if (transaction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repo.delete(transaction);

    }
}
