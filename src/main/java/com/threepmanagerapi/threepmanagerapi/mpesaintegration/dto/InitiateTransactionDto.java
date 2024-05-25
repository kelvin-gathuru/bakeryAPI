package com.threepmanagerapi.threepmanagerapi.mpesaintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitiateTransactionDto {
    private String productDispatchCode;
    private BigDecimal amount;
    private String narration;
    private String msisdn;
}
