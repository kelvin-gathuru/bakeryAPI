package com.threepmanagerapi.threepmanagerapi.client.repository;

import com.threepmanagerapi.threepmanagerapi.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findByPhone(String phone);

}
