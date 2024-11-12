package com.threepmanagerapi.threepmanagerapi.productdispatch.model;

import com.threepmanagerapi.threepmanagerapi.client.model.Client;
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
public class ClientsDispatchedProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientsDispatchedProductID;
    private BigDecimal deliveredQuantity;
    private BigDecimal deliveredProductPrice;
    private String productDispatchCode;
    @OneToOne
    @JoinColumn(name="createdBy")
    private User user;
    private Long productID;
    private String name;
    private String metric;
    private BigDecimal unitPrice;
    private LocalDateTime saleDate;
    private Long clientID;
    @OneToOne
    private Client client;
}
