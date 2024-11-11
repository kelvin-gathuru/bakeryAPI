package com.threepmanagerapi.threepmanagerapi.productdispatch.repository;

import com.threepmanagerapi.threepmanagerapi.productdispatch.model.ClientsDispatchedProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDispatchedProductsRepository extends JpaRepository<ClientsDispatchedProducts,Long> {
}
