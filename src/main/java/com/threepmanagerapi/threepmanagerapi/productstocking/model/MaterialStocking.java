package com.threepmanagerapi.threepmanagerapi.productstocking.model;

import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import com.threepmanagerapi.threepmanagerapi.supplier.model.Supplier;
import com.threepmanagerapi.threepmanagerapi.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MaterialStocking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productStockingID;
    private BigDecimal quantity;
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
}
