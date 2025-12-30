package com.budgetapp.budgetapi.controller;

import com.budgetapp.budgetapi.model.transaction.TransactionModel;
import com.budgetapp.budgetapi.model.user.UserPrincipal;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.UserRepo;
import com.budgetapp.budgetapi.service.TransactionService;
import com.budgetapp.budgetapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService service;
    private final UserRepo userRepo;

    @Autowired
    public TransactionController(TransactionService service, UserRepo userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }


    @GetMapping("/transactions")
    public List<TransactionModel> getTransactions(@AuthenticationPrincipal UserPrincipal principal) {
        String username = principal.getUsername();
        Users user = userRepo.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return service.getTransactions(user.getId());
    }

    @PostMapping("/transaction")
    public TransactionModel addTransaction(@RequestBody TransactionModel transaction) {
        return service.addTransaction(transaction);
    }

}
