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

    public BucketModel getBucket(String bucketName, Integer userId) {

        return bucketRepo.findByNameAndUserId(bucketName, userId);


    }

    public List<BucketDto> getBuckets(Integer userId) {


        return bucketRepo.findAllByUserId(userId)
                .stream()
                .map(bucket ->{
                    BucketDto bucketDto = new BucketDto();
                    bucketDto.setName(bucket.getName());
                    bucketDto.setBankAccount(bucket.getBankAccount().getName());
                    return bucketDto;
                })
                .toList();
    }

    public BucketDto addBucket(BucketDto bucketDto, Users user) {
        BucketModel bucketModel = new BucketModel();
        BankAccountModel bankAccountModel = bankAccountRepo.findByNameAndUserId(bucketDto.getBankAccount(), user.getId());
        if(bankAccountModel == null) {
            return null;
        }
        bucketModel.setBankAccount(bankAccountModel);
        bucketModel.setName(bucketDto.getName());
        bucketModel.setUser(user);
        bucketRepo.save(bucketModel);
        return bucketDto;


    }
}
