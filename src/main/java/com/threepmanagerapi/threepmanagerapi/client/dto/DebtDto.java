package com.threepmanagerapi.threepmanagerapi.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtDto {
    private Long clientID;
    private BigDecimal amount;
}
