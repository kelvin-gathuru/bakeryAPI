package com.threepmanagerapi.threepmanagerapi.client.repository;

import com.threepmanagerapi.threepmanagerapi.client.model.Client;
import com.threepmanagerapi.threepmanagerapi.region.model.Region;
import com.threepmanagerapi.threepmanagerapi.settings.Model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findByPhone(String phone);
    Client findByClientID(Long id);

    List<Client> findByRegion(Region region);
    List<Client> findByStatus(Status status);

}
