package com.budgetapp.budgetapi.controller;

import com.budgetapp.budgetapi.model.transaction.BucketModel;
import com.budgetapp.budgetapi.model.user.UserPrincipal;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.service.BucketService;
import com.budgetapp.budgetapi.service.dto.BucketDto;
import com.budgetapp.budgetapi.util.UserVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BucketController {

    private final BucketService service;
    private final UserVerify userVerify;

    @Autowired
    public BucketController(BucketService service, UserVerify userVerify) {
        this.userVerify = userVerify;
        this.service = service;
    }

    @GetMapping("/buckets")
    public List<BucketDto> getBuckets(@AuthenticationPrincipal UserPrincipal principal) {
        Users user = userVerify.verifyUser(principal);
        return service.getBuckets(user.getId());
    }

    @PostMapping("/bucket")
    public BucketDto addBucket(@AuthenticationPrincipal UserPrincipal principal, @RequestBody BucketDto bucketDto) {
        Users user = userVerify.verifyUser(principal);
        return service.addBucket(bucketDto, user);
    }

}
