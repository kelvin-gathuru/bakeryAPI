package com.threepmanagerapi.threepmanagerapi.region.repository;

import com.threepmanagerapi.threepmanagerapi.region.model.Region;
import com.threepmanagerapi.threepmanagerapi.settings.Model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByName(String name);
    Region findByRegionID(Long name);
    List<Region> findByStatus(Status status);
}
