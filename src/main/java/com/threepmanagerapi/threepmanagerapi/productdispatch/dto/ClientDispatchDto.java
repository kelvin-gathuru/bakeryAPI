package com.threepmanagerapi.threepmanagerapi.productdispatch.dto;

import com.threepmanagerapi.threepmanagerapi.client.model.RegistrationType;
import com.threepmanagerapi.threepmanagerapi.client.model.SalesType;
import com.threepmanagerapi.threepmanagerapi.productdispatch.model.DispatchedProducts;
import com.threepmanagerapi.threepmanagerapi.region.model.Region;
import com.threepmanagerapi.threepmanagerapi.settings.Model.Status;
import com.threepmanagerapi.threepmanagerapi.user.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDispatchDto {
    private Long clientID;
    private String name;
    private String phone;
    private String email;
    @Enumerated(EnumType.STRING)
    private SalesType salesType;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private RegistrationType registrationType;
    @OneToOne
    @JoinColumn(name = "regionID")
    private Region region;
    @OneToOne
    @JoinColumn(name = "userID")
    private User user;
    private BigDecimal cumulativeAmountToPay;
    private BigDecimal cumulativeAmountPaid;
    private BigDecimal cumulativeAmountBalance;
    private BigDecimal cumulativeCratesOut;
    private BigDecimal cumulativeCratesIn;
    private List<DispatchedProducts> dispatchedProducts;

}
