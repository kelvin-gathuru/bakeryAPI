package com.threepmanagerapi.threepmanagerapi.supplier.repository;

import com.threepmanagerapi.threepmanagerapi.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Supplier findByPhone (String phone);
    Supplier findBySupplierID (Long id);
}
