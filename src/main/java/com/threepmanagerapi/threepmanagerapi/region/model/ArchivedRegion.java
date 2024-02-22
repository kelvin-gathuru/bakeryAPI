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
public class ArchivedRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long archivedRegionID;
    private String name;
    @OneToOne
    @JoinColumn(name="userID")
    private User user;
    @Enumerated(EnumType.STRING)
    private Status status;
}
