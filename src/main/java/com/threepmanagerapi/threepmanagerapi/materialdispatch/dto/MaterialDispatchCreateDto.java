package com.threepmanagerapi.threepmanagerapi.materialdispatch.dto;

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
public class MaterialDispatchCreateDto {
    private Long materialDispatchID;
    private BigDecimal initialQuantity;
    private BigDecimal updatedQuantity;
    private LocalDateTime dispatchDate;
    @OneToOne
    @JoinColumn(name="materialID")
    private Material material;
    private String shift;
    private String description;
}
