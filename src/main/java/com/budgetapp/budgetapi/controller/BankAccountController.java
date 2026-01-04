package com.budgetapp.budgetapi.controller;

import com.budgetapp.budgetapi.model.user.UserPrincipal;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.service.BankAccountService;
import com.budgetapp.budgetapi.util.UserVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankAccountController {

    private final BankAccountService service;
    private final UserVerify userVerify;

    @Autowired
    public BankAccountController(BankAccountService service, UserVerify userVerify) {
        this.userVerify = userVerify;
        this.service = service;
    }

    @GetMapping("/bankaccounts")
    public List<String> getBuckets(@AuthenticationPrincipal UserPrincipal principal) {
        Users user = userVerify.verifyUser(principal);
        return service.getBankAccounts(user.getId());
    }

}
