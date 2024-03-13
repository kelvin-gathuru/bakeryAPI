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
    private BigDecimal quantity;
    private BigDecimal remainingStock;
    private BigDecimal totalPrice;
    private BigDecimal returnedQuantity;
    private BigDecimal returnedSpoiled;
    private BigDecimal soldQuantity;
    private BigDecimal salesPrice;
//    @OneToOne
//    @JoinColumn(name="product")
//    private Product product;
//    @ManyToOne
//    @JoinColumn(name = "productDispatchCode")
    private String productDispatchCode;
    @OneToOne
    @JoinColumn(name="createdBy")
    private User user;
    private Long productID;
    private String name;
    private String metric;
    private BigDecimal unitPrice;
    private BigDecimal reorderPoint;
    private BigDecimal reorderQuantity;
    private BigDecimal remainingQuantity;
    private LocalDateTime dateCreated;
    private LocalDateTime saleDate;
    private  BigDecimal totalSalesForRetrieval;
}
