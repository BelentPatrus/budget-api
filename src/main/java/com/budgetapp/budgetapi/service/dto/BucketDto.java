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
public class BucketDto {

    private Long id;
    private String name;
    private String bankAccount;
    private long bankAccountId;
    private BigDecimal balance;

}
