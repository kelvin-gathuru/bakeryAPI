package com.threepmanagerapi.threepmanagerapi.productdispatch.model;
import com.threepmanagerapi.threepmanagerapi.client.model.Client;
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
    private BigDecimal totalDispatchPrice;
    private String vehicle;
    @OneToOne
    @JoinColumn(name="client")
    private Client client;
    @OneToMany(mappedBy = "dispatchedProductID", cascade = CascadeType.ALL)
    private List<DispatchedProducts> dispatchedProducts;
}
