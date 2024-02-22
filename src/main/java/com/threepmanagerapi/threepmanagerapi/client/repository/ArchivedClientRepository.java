package com.threepmanagerapi.threepmanagerapi.client.repository;

import com.threepmanagerapi.threepmanagerapi.client.model.ArchivedClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivedClientRepository extends JpaRepository<ArchivedClient,Long> {
}
