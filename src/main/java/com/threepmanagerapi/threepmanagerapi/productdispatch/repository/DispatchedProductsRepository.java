package com.threepmanagerapi.threepmanagerapi.productdispatch.repository;

import com.threepmanagerapi.threepmanagerapi.productdispatch.model.DispatchedProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DispatchedProductsRepository extends JpaRepository<DispatchedProducts,Long> {
    List<DispatchedProducts> findByDispatchedProductID(Long id);
    List<DispatchedProducts> findByProductDispatchCode(String code);
}
