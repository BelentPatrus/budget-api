package com.budgetapp.budgetapi.service;

import com.budgetapp.budgetapi.model.transaction.BankAccountModel;
import com.budgetapp.budgetapi.model.transaction.BucketModel;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.BankAccountRepo;
import com.budgetapp.budgetapi.repo.BucketRepo;
import com.budgetapp.budgetapi.service.dto.BucketDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BucketService {

    private final BucketRepo bucketRepo;
    private final BankAccountRepo bankAccountRepo;

    public BucketService(BucketRepo bucketRepo, BankAccountRepo bankAccountRepo) {
        this.bucketRepo = bucketRepo;
        this.bankAccountRepo = bankAccountRepo;
    }

    public BucketModel getBucket(String bucketName, Long userId) {

        return bucketRepo.findByNameAndUserId(bucketName, userId);


    }

    public List<BucketDto> getBuckets(Long userId) {


        return bucketRepo.findAllByUserId(userId)
                .stream()
                .map(bucket ->{
                    BucketDto bucketDto = new BucketDto();
                    bucketDto.setName(bucket.getName());
                    bucketDto.setBankAccount(bucket.getBankAccount().getName());
                    bucketDto.setBankAccountId(bucket.getBankAccount().getId());
                    bucketDto.setId(bucket.getId());
                    bucketDto.setBalance(bucket.getBalance());
                    return bucketDto;
                })
                .toList();
    }

    public BucketDto addBucket(BucketDto bucketDto, Users user) {
        BucketModel bucketModel = new BucketModel();
        BankAccountModel bankAccountModel;

        if(bucketDto.getBankAccount() != null && !bucketDto.getBankAccount().isEmpty()){
            bankAccountModel = bankAccountRepo.findByNameAndUserId(bucketDto.getBankAccount(), user.getId());

        }else{
            bankAccountModel = bankAccountRepo.findByIdAndUserId(bucketDto.getBankAccountId(), user.getId());
        }
        if(bankAccountModel == null) {
            return null;
        }
        bucketModel.setBankAccount(bankAccountModel);
        bucketModel.setName(bucketDto.getName());
        bucketModel.setUser(user);
        bucketModel.setBalance(bucketDto.getBalance());
        bucketRepo.save(bucketModel);
        return bucketDto;


    }

    public BucketModel getBucketById(String bucket, Long id) {
        return bucketRepo.findByIdAndUserId(Long.parseLong(bucket), id);
    }

    public void updateBucket(BucketModel bucket) {
        bucketRepo.save(bucket);
    }
}
