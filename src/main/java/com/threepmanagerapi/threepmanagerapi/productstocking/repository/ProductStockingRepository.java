package com.threepmanagerapi.threepmanagerapi.productstocking.repository;

import com.threepmanagerapi.threepmanagerapi.productstocking.model.ProductStocking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockingRepository extends JpaRepository<ProductStocking, Long> {
    ProductStocking findByProductStockID(Long id);
}
