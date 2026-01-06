package com.budgetapp.budgetapi.controller;

import com.budgetapp.budgetapi.model.transaction.TransactionModel;
import com.budgetapp.budgetapi.model.user.UserPrincipal;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.UserRepo;
import com.budgetapp.budgetapi.service.TransactionService;
import com.budgetapp.budgetapi.service.dto.TransactionDto;
import com.budgetapp.budgetapi.util.BankCSVParser;
import com.budgetapp.budgetapi.util.UserVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService service;
    private final UserVerify userVerify;


    @Autowired
    public TransactionController(TransactionService service, UserVerify userVerify) {
        this.service = service;
        this.userVerify = userVerify;

    }


    @GetMapping("/transactions")
    public List<TransactionModel> getTransactions(@AuthenticationPrincipal UserPrincipal principal) {
        Users user = userVerify.verifyUser(principal);
        return service.getTransactions(user.getId());
    }

    @DeleteMapping ("/transaction/{id}")
    public void deleteTransaction(@AuthenticationPrincipal UserPrincipal principal, @PathVariable String id) {
        Users user = userVerify.verifyUser(principal);
        service.deleteTransactions(user.getId(), Long.parseLong(id));
    }


    @PostMapping("/transaction")
    public TransactionDto addTransaction(@AuthenticationPrincipal UserPrincipal principal,@RequestBody TransactionDto transactionDto) {
        Users user = userVerify.verifyUser(principal);
        return service.addTransaction(transactionDto, user);
    }

    @PostMapping(
            value = "/transactions/import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> importTransactions(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        // Optional: basic file type check
        String name = (file.getOriginalFilename() == null) ? "" : file.getOriginalFilename().toLowerCase();
        if (!name.endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Only .csv supported for now");
        }
        List<BankCSVParser.ImportedTransactionRow>  rows = service.importTransactions(file);

        return ResponseEntity.ok(rows);
    }



}
