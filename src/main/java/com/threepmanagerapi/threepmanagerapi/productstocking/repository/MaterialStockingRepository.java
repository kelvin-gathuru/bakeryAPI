package com.threepmanagerapi.threepmanagerapi.productstocking.repository;

import com.threepmanagerapi.threepmanagerapi.productstocking.model.MaterialStocking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialStockingRepository extends JpaRepository<MaterialStocking, Long> {
    MaterialStocking findByProductStockingID(Long id);
}
