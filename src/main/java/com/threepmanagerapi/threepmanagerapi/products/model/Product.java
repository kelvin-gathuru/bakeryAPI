package com.threepmanagerapi.threepmanagerapi.products.model;

import com.threepmanagerapi.threepmanagerapi.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;
    private String name;
    private String metric;
    private BigDecimal unitPrice;
    private BigDecimal reorderPoint;
    private BigDecimal reorderQuantity;
    private BigDecimal remainingQuantity;
    private LocalDateTime dateCreated;
    @OneToOne
    @JoinColumn(name="createdBy")
    private User user;
}
