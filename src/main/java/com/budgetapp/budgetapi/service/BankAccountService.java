package com.budgetapp.budgetapi.service;

import com.budgetapp.budgetapi.model.transaction.BankAccountModel;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.BankAccountRepo;
import com.budgetapp.budgetapi.repo.BucketRepo;
import com.budgetapp.budgetapi.service.dto.BankAccountDTO;
import com.budgetapp.budgetapi.service.dto.BucketDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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

    public BankAccountModel getBankAccount(String name, Long userId) {
        return bankAccountRepo.findByNameAndUserId(name, userId);
    }

    public List<BankAccountDTO> getBankAccounts(Long userId) {
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

    public List<BucketDto> getBuckets(Long userId, Long accountId) {
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

    public void deleteBankAccount(Long userId, Long accountId) {
        BankAccountModel bankAccount = bankAccountRepo.findByIdAndUserId(accountId, userId);
        if (bankAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(bankAccount.getBalance().compareTo(BigDecimal.ZERO) != 0){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Account has buckets. Remove buckets first."
            );
        }
        if(bucketRepo.findAllByBankAccountIdAndUserId(accountId, userId).size() != 0){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Account balance must be zero before deletion"
            );
        }
        bankAccountRepo.delete(bankAccount);

    }
}
