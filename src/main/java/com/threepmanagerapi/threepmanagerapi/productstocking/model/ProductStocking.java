package com.threepmanagerapi.threepmanagerapi.productstocking.model;

import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import com.threepmanagerapi.threepmanagerapi.products.model.Product;
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
public class ProductStocking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productStockID;
    private BigDecimal quantity;
    private BigDecimal spoiledAtProduction;
    private BigDecimal spoiledAtPackaging;
    private BigDecimal totalPrice;
    private LocalDateTime stockDate;
    @OneToOne
    @JoinColumn(name="productID")
    private Product product;
    @OneToOne
    @JoinColumn(name="createdBy")
    private User user;
    private String description;
}
