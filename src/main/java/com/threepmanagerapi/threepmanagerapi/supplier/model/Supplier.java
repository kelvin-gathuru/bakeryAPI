package com.threepmanagerapi.threepmanagerapi.supplier.model;

import com.threepmanagerapi.threepmanagerapi.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierID;
    private String name;
    private String phone;
    private String alternativeContact;
    private String physicalAddress;
    @OneToOne
    @JoinColumn(name = "userID")
    private User user;

    private BigDecimal cumulativeAmountToPay;
    private BigDecimal cumulativeAmountPaid;
    private BigDecimal cumulativeAmountBalance;
    private BigDecimal cumulativeCratesOut;
    private BigDecimal cumulativeCratesIn;
}
