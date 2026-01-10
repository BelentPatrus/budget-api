package com.budgetapp.budgetapi.service;

import com.budgetapp.budgetapi.model.transaction.BankAccountModel;
import com.budgetapp.budgetapi.model.transaction.BucketModel;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.BankAccountRepo;
import com.budgetapp.budgetapi.repo.BucketRepo;
import com.budgetapp.budgetapi.service.dto.BankAccountDTO;
import com.budgetapp.budgetapi.service.dto.BucketDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {

    private final BankAccountRepo bankAccountRepo;
    private final BucketRepo bucketRepo;

    @Autowired
    public BankAccountService(BankAccountRepo bankAccountRepo, BucketRepo bucketRepo) {
        this.bucketRepo = bucketRepo;
        this.bankAccountRepo = bankAccountRepo;
    }

    public BankAccountModel getBankAccount(String name, Integer userId) {
        return bankAccountRepo.findByNameAndUserId(name, userId);
    }

    public List<BankAccountDTO> getBankAccounts(Integer userId) {
        return bankAccountRepo.findAllByUserId(userId)
                .stream()
                .map(bucket -> {
                    BankAccountDTO dto = new BankAccountDTO();
                    dto.setId(bucket.getId());
                    dto.setName(bucket.getName());
                    dto.setCreditOrDebit(bucket.getCreditOrDebit());
                    dto.setBalance(bucket.getBalance());
                    return dto;
                })
                .toList();
    }

    public List<BucketDto> getBuckets(Integer userId, Long accountId) {
        return bucketRepo.findAllByBankAccountIdAndUserId(accountId, userId)
                .stream()
                .map(bucket -> {
                    BucketDto dto = new BucketDto();
                    dto.setId(bucket.getId());
                    dto.setName(bucket.getName());
                    dto.setBankAccountId(bucket.getBankAccount().getId());
                    dto.setBankAccount(bucket.getBankAccount().getName());
                    dto.setBalance(bucket.getBalance());
                    return dto;
                })
                .toList();


    }

    public BankAccountDTO addBankAccount(Users user, BankAccountDTO bankAccountDTO) {
        BankAccountModel bankAccountModel = new BankAccountModel();
        bankAccountModel.setName(bankAccountDTO.getName());
        bankAccountModel.setCreditOrDebit(bankAccountDTO.getCreditOrDebit());
        bankAccountModel.setBalance(bankAccountDTO.getBalance());
        bankAccountModel.setUser(user);
        bankAccountRepo.save(bankAccountModel);
        return bankAccountDTO;
    }

    public void updateBankAccount(BankAccountModel bankAccount) {
        bankAccountRepo.save(bankAccount);
    }
}
