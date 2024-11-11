package com.threepmanagerapi.threepmanagerapi.productdispatch.model;

import com.threepmanagerapi.threepmanagerapi.client.model.Client;
import com.threepmanagerapi.threepmanagerapi.supplier.model.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ClientsProductDispatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ClientsProductDispatchID;
    private String productDispatchCode;
    private BigDecimal totalDispatchPrice;
    private BigDecimal amountPaid;
    private BigDecimal balance;
    private String paymentMode;
    @OneToOne
    @JoinColumn(name="client")
    private Client client;
    @OneToMany(mappedBy = "dispatchedProductID", cascade = CascadeType.ALL)
    private List<DispatchedProducts> dispatchedProducts;
    private LocalDateTime paymentDate;
    private LocalDateTime dispatchDate;
}

