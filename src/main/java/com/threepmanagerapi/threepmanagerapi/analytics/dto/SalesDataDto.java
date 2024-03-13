package com.threepmanagerapi.threepmanagerapi.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesDataDto {
    private String day;
    private BigDecimal amount;
}
