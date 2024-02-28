package com.threepmanagerapi.threepmanagerapi.products.repository;

import com.threepmanagerapi.threepmanagerapi.productdispatch.model.DispatchedProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DispatchedProductsRepository extends JpaRepository<DispatchedProducts,Long> {
}
