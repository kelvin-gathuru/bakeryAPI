package com.threepmanagerapi.threepmanagerapi.productstocking.dto;

import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import com.threepmanagerapi.threepmanagerapi.products.model.Product;
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
public class ProductStockUpdateDto {
    private Long productStockID;
    private BigDecimal initialQuantity;
    private BigDecimal updatedQuantity;
    private BigDecimal spoiledAtProduction;
    private BigDecimal spoiledAtPackaging;
    private LocalDateTime stockDate;
    @OneToOne
    @JoinColumn(name="productID")
    private Product product;
    @OneToOne
    @JoinColumn(name="createdBy")
    private User user;
    private String description;
}
