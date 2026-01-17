package com.budgetapp.budgetapi.service.dto;

import com.budgetapp.budgetapi.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    String id;
    String date;
    String description;
    String bucket;
    String account;
    BigDecimal amount;
    TransactionType transactionType;
}
