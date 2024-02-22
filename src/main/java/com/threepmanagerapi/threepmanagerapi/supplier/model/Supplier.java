package com.threepmanagerapi.threepmanagerapi.supplier.model;

import com.threepmanagerapi.threepmanagerapi.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @OneToOne
    @JoinColumn(name = "userID")
    private User user;
}
