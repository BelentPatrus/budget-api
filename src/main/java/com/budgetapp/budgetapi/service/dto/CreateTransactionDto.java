package com.budgetapp.budgetapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionDto {
    String date;
    String description;
    String bucket;
    String account;
    BigDecimal amount;
    String incomeOrExpense;
}
