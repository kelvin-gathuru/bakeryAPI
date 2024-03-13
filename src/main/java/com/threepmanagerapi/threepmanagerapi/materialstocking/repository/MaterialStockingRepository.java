package com.threepmanagerapi.threepmanagerapi.materialstocking.repository;

import com.threepmanagerapi.threepmanagerapi.materialstocking.model.MaterialStocking;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialStockingRepository extends JpaRepository<MaterialStocking, Long> {
    MaterialStocking findByMaterialStockID(Long id);
    public List<MaterialStocking> findAll (Specification<MaterialStocking> specification);
}
