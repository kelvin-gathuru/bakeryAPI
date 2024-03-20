package com.threepmanagerapi.threepmanagerapi.client.model;

import com.threepmanagerapi.threepmanagerapi.region.model.Region;
import com.threepmanagerapi.threepmanagerapi.settings.Model.Status;
import com.threepmanagerapi.threepmanagerapi.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
