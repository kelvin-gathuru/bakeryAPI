package com.threepmanagerapi.threepmanagerapi.productdispatch.specification;

import com.threepmanagerapi.threepmanagerapi.productdispatch.model.ProductDispatch;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDispatchSpecification {
    public Specification<ProductDispatch> getProductDispatchReturned(String startDate, String endDate){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("returned"), true));
            if (startDate != null && !startDate.isEmpty()) {
                LocalDateTime parsedStartDate = LocalDateTime.parse(startDate);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("returnedDate"), parsedStartDate));
            }
            if (endDate != null && !endDate.isEmpty()) {
                LocalDateTime parsedEndDate = LocalDateTime.parse(endDate);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("returnedDate"), parsedEndDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
