package com.threepmanagerapi.threepmanagerapi.materialdispatch.repository;

import com.threepmanagerapi.threepmanagerapi.materialdispatch.model.MaterialDispatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialDispatchRepository extends JpaRepository<MaterialDispatch, Long> {
    MaterialDispatch findByMaterialDispatchID(Long id);
}
