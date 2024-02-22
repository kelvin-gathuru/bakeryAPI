package com.threepmanagerapi.threepmanagerapi.materials.repository;

import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    Material findByMaterialID(Long id);
    Material findByName(String name);

    List<Material> findByDateCreated(LocalDateTime date);
}
