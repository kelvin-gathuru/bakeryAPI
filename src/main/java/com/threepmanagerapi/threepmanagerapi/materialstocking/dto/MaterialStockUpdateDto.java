package com.threepmanagerapi.threepmanagerapi.materialstocking.dto;

import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import com.threepmanagerapi.threepmanagerapi.supplier.model.Supplier;
import com.threepmanagerapi.threepmanagerapi.user.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialStockUpdateDto {
    private Long materialStockID;
    private BigDecimal initialQuantity;
    private BigDecimal updatedQuantity;
    private LocalDateTime purchaseDate;
    @OneToOne
    @JoinColumn(name="materialID")
    private Material material;
    @OneToOne
    @JoinColumn(name="supplierID")
    private Supplier supplier;
    @OneToOne
    @JoinColumn(name="createdBy")
    private User user;
    private String description;
}
