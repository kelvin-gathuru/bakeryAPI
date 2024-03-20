package com.threepmanagerapi.threepmanagerapi.productdispatch.repository;

import com.threepmanagerapi.threepmanagerapi.client.model.Client;
import com.threepmanagerapi.threepmanagerapi.productdispatch.model.ProductDispatch;
import com.threepmanagerapi.threepmanagerapi.productdispatch.specification.ProductDispatchSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductDispatchRepository extends JpaRepository<ProductDispatch, Long> {
    ProductDispatch findByProductDispatchCode(String code);
    ProductDispatch findByProductDispatchID(Long id);
    List<ProductDispatch> findByReturned(boolean returned);
    List<ProductDispatch> findByReturnedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<ProductDispatch> findByClient(Client client);
    public List<ProductDispatch> findAll (Specification<ProductDispatch> specification);
}
