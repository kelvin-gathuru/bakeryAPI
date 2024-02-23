package com.threepmanagerapi.threepmanagerapi.materialstocking.repository;

import com.threepmanagerapi.threepmanagerapi.materialstocking.model.MaterialStocking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialStockingRepository extends JpaRepository<MaterialStocking, Long> {
    MaterialStocking findByMaterialStockID(Long id);
}
