package com.threepmanagerapi.threepmanagerapi.products.repository;
import com.threepmanagerapi.threepmanagerapi.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductID(Long id);
    Product findByName(String name);

    List<Product> findByDateCreated(LocalDateTime date);
}
