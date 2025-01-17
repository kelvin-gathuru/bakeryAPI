package com.threepmanagerapi.threepmanagerapi.productdispatch.model;
import com.threepmanagerapi.threepmanagerapi.client.model.Client;
import com.threepmanagerapi.threepmanagerapi.supplier.model.Supplier;
import com.threepmanagerapi.threepmanagerapi.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ProductDispatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productDispatchID;
    private String productDispatchCode;
    private BigDecimal fuelIssued;
    private BigDecimal cratesOut;
    private BigDecimal cratesIn;
    private BigDecimal totalDispatchPrice;
    private BigDecimal totalSalesPrice;
    private BigDecimal amountPaid;
    private BigDecimal balance;
    private BigDecimal expenses;
    private String vehicle;
    private String paymentMode;
    @OneToOne
    @JoinColumn(name="supplier")
    private Supplier supplier;
    @OneToMany(mappedBy = "dispatchedProductID", cascade = CascadeType.ALL)
    private List<DispatchedProducts> dispatchedProducts;
    private boolean returned;
    private LocalDateTime returnedDate;
    private LocalDateTime dispatchDate;
    private Long overdueDays;
    private Long daysTaken;
}
