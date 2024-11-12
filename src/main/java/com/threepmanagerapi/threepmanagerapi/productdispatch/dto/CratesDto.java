package com.threepmanagerapi.threepmanagerapi.productdispatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CratesDto {
    private Long supplierID;
    private BigDecimal crates;
}
