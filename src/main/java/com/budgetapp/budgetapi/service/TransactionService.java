package com.budgetapp.budgetapi.service;


//rules of the game:
//
//        income transaction    --> INCOME_RULE: income goes to a debit account & bucket (could be unallocated meta bucket)
//
//        expense transaction 	--> EXPENSE_DEBIT_RULE: if debit account, take out of bucket (could be unallocated meta bucket) and account
//                              --> EPENSE_CREDIT_RULE: if credit account, no bucket just account
//
//        transfer transaction	--> debit to debit account: EXPENSE_DEBIT_RULE -> INCOME_RULE
//                              --> debit to credit account: EXPENSE_DEBIT_RULE -> into account
//                              --> credit to debit account: EPENSE_CREDIT_RULE -> into account
//                              --> credit to credit account: EPENSE_CREDIT_RULE --> into account

import com.budgetapp.budgetapi.model.enums.CreditOrDebit;
import com.budgetapp.budgetapi.model.enums.TransactionType;
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

import java.time.LocalDate;
import java.util.List;

import static com.budgetapp.budgetapi.model.enums.TransactionType.EXPENSE;
import static com.budgetapp.budgetapi.model.enums.TransactionType.INCOME;

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

    public List<TransactionModel> getTransactions(Long userId) {
        return repo.findByUserId(userId);
    }

    public TransactionDto addTransaction(TransactionDto transactionDto, Users user) {
        TransactionModel transaction = new TransactionModel();
        transaction.setUser(user);
        transaction.setDescription(transactionDto.getDescription());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDate(LocalDate.parse(transactionDto.getDate()));
        transaction.setTransactionType(transactionDto.getIncomeOrExpense().equalsIgnoreCase("INCOME") ? INCOME : EXPENSE);
        if(transactionDto.getBucket() != null) {
            transaction.setBucket(bucketService.getBucketById(transactionDto.getBucket(), user.getId()));
        }
        transaction.setBankAccount(bankAccountService.getBankAccount(transactionDto.getAccount(), user.getId()));
        repo.save(transaction);
        handleBalanceUpdates(transaction);
        transactionDto.setId(transaction.getId().toString());
        return transactionDto;

    }

    private void expenseOrIncomeDebitAccountRule(TransactionModel transaction){
        if(transaction.getBucket() != null){
            transaction.getBucket().setBalance(transaction.getBucket().getBalance().add(transaction.getAmount()));
            bucketService.updateBucket(transaction.getBucket());
        }
        transaction.getBankAccount().setBalance(transaction.getBankAccount().getBalance().add(transaction.getAmount()));
        bankAccountService.updateBankAccount(transaction.getBankAccount());
    }

    private void expenseCreditRule(TransactionModel transaction){
        transaction.getBankAccount().setBalance(transaction.getBankAccount().getBalance().add(transaction.getAmount().abs()));
        bankAccountService.updateBankAccount(transaction.getBankAccount());
    }

    private void handleBalanceUpdates(TransactionModel transaction) {
        switch (transaction.getTransactionType()) {
            case INCOME:
                expenseOrIncomeDebitAccountRule(transaction);
                break;
            case EXPENSE:
                if(transaction.getBankAccount().getCreditOrDebit() == CreditOrDebit.DEBIT) {
                    expenseOrIncomeDebitAccountRule(transaction);
                }else{
                    expenseCreditRule(transaction);
                }
                break;
        }
    }

    public void deleteTransactions(Long userId, Long id ) {
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
