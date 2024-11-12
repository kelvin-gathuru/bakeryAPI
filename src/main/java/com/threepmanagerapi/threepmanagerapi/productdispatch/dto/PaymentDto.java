package com.threepmanagerapi.threepmanagerapi.productdispatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDto {
    private Long payerID;
    private String paymentMode;
    private BigDecimal amount;
}
