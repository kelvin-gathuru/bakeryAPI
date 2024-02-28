package com.threepmanagerapi.threepmanagerapi.productdispatch.repository;

import com.threepmanagerapi.threepmanagerapi.productdispatch.model.ProductDispatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDispatchRepository extends JpaRepository<ProductDispatch, Long> {
    ProductDispatch findByProductDispatchCode(String code);
    ProductDispatch findByProductDispatchID(Long id);
}
