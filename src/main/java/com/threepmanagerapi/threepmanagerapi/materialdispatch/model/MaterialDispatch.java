package com.threepmanagerapi.threepmanagerapi.materialdispatch.model;

import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
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
public class MaterialDispatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialDispatchID;
    private BigDecimal quantity;
    private BigDecimal totalPrice;
    private LocalDateTime dispatchDate;
    @OneToOne
    @JoinColumn(name="materialID")
    private Material material;
    @OneToOne
    @JoinColumn(name="createdBy")
    private User user;
    private String description;
    @Enumerated(EnumType.STRING)
    private Shift shift;
}
