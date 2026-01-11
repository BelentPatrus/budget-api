package com.budgetapp.budgetapi.service.dto;

import com.budgetapp.budgetapi.model.enums.CreditOrDebit;
import com.budgetapp.budgetapi.model.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {
    private Long id;
    private String name;
    private CreditOrDebit creditOrDebit;
    private BigDecimal balance;
    private ActiveStatus status;

}
