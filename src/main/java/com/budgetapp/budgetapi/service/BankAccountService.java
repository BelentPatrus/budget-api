package com.budgetapp.budgetapi.service;

import com.budgetapp.budgetapi.model.transaction.BankAccountModel;
import com.budgetapp.budgetapi.model.transaction.BucketModel;
import com.budgetapp.budgetapi.repo.BankAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {

    private final BankAccountRepo bankAccountRepo;

    @Autowired
    public BankAccountService(BankAccountRepo bankAccountRepo) {
        this.bankAccountRepo = bankAccountRepo;
    }

    public BankAccountModel getBankAccount(String name, Integer userId) {
        return bankAccountRepo.findByNameAndUserId(name, userId);
    }

    public List<String> getBankAccounts(Integer userId) {
        return bankAccountRepo.findAllByUserId(userId).stream().map(BankAccountModel::getName).toList();
    }

}
