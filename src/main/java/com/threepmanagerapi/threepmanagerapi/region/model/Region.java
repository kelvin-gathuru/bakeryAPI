package com.threepmanagerapi.threepmanagerapi.region.model;

import com.threepmanagerapi.threepmanagerapi.settings.Model.Status;
import com.threepmanagerapi.threepmanagerapi.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionID;
    private String name;
    @ManyToOne
    @JoinColumn(name="createdBy")
    private User user;
    @Enumerated(EnumType.STRING)
    private Status status;
}
