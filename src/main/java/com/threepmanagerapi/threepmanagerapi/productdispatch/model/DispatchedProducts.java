package com.threepmanagerapi.threepmanagerapi.productdispatch.model;

import com.threepmanagerapi.threepmanagerapi.products.model.Product;
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
public class DispatchedProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dispatchedProductID;
    private BigDecimal dispatchedQuantity;
    private BigDecimal dispatchPrice;
    @OneToOne
    @JoinColumn(name="product")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "productDispatchCode") // This refers to the column name in the DispatchDetails table
    private ProductDispatch productDispatch;
}
