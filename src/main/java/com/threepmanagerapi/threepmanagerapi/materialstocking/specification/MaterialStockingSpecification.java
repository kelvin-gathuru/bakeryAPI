package com.threepmanagerapi.threepmanagerapi.materialstocking.specification;

import com.threepmanagerapi.threepmanagerapi.materialstocking.model.MaterialStocking;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MaterialStockingSpecification {
    public Specification<MaterialStocking> getMaterialStocking(String startDate, String endDate){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (startDate != null && !startDate.isEmpty()) {
                LocalDateTime parsedStartDate = LocalDateTime.parse(startDate);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("purchaseDate"), parsedStartDate));
            }
            if (endDate != null && !endDate.isEmpty()) {
                LocalDateTime parsedEndDate = LocalDateTime.parse(endDate);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("purchaseDate"), parsedEndDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
