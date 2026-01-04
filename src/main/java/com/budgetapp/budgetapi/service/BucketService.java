package com.budgetapp.budgetapi.service;

import com.budgetapp.budgetapi.model.transaction.BucketModel;
import com.budgetapp.budgetapi.repo.BucketRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BucketService {

    private final BucketRepo bucketRepo;

    public BucketService(BucketRepo bucketRepo) {
        this.bucketRepo = bucketRepo;
    }

    public BucketModel getBucket(String bucketName, Integer userId) {

        return bucketRepo.findByNameAndUserId(bucketName, userId);


    }

    public List<String> getBuckets(Integer userId) {
        return bucketRepo.findAllByUserId(userId).stream().map(BucketModel::getName).toList();
    }
}
