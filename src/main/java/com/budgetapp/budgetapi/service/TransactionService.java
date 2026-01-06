package com.budgetapp.budgetapi.service;


import com.budgetapp.budgetapi.model.enums.IncomeOrExpense;
import com.budgetapp.budgetapi.model.transaction.TransactionModel;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.TransactionRepo;
import com.budgetapp.budgetapi.service.dto.TransactionDto;
import com.budgetapp.budgetapi.util.BankCSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepo repo;
    private final BucketService bucketService;
    private final BankAccountService bankAccountService;
    private final BankCSVParser bankCSVParser;

    @Autowired
    public TransactionService(TransactionRepo repo, BucketService bucketService, BankAccountService bankAccountService, BankCSVParser bankCSVParser) {
        this.bucketService = bucketService;
        this.repo = repo;
        this.bankAccountService = bankAccountService;
        this.bankCSVParser = bankCSVParser;
    }

    public List<TransactionModel> getTransactions(int userId) {
        return repo.findByUserId(userId);
    }

    public TransactionDto addTransaction(TransactionDto transactionDto, Users user) {
        TransactionModel transaction = new TransactionModel();
        transaction.setUser(user);
        transaction.setDescription(transactionDto.getDescription());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDate(LocalDate.parse(transactionDto.getDate()));
        transaction.setIncomeOrExpense(transactionDto.getIncomeOrExpense().equalsIgnoreCase("INCOME") ? IncomeOrExpense.INCOME : IncomeOrExpense.EXPENSE);
        transaction.setBucket(bucketService.getBucket(transactionDto.getBucket(), user.getId()));
        transaction.setBankAccount(bankAccountService.getBankAccount(transactionDto.getAccount(), user.getId()));
        repo.save(transaction);
        transactionDto.setId(transaction.getId().toString());
        return transactionDto;

    }

    public void deleteTransactions(int userId, Long id ) {
        TransactionModel transaction = repo.findByIdAndUserId(id,userId);
        if (transaction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repo.delete(transaction);

    }

    public List<BankCSVParser.ImportedTransactionRow> importTransactions(MultipartFile file) {
        List<BankCSVParser.ImportedTransactionRow> rows = bankCSVParser.parse(file);
        return rows;
    }

}
