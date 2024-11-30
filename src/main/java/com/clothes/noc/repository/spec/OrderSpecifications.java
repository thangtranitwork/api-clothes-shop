package com.clothes.noc.repository.spec;

import com.clothes.noc.dto.request.SearchOrderRequest;
import com.clothes.noc.entity.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecifications {
    public static Specification<Order> multipleFieldsSearch(String userId, SearchOrderRequest request) {
        return ((root, query, criteriaBuilder) -> {
           List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.join("user").get("id"), userId));

           if(request.getId() != null && !request.getId().isEmpty()) {
               predicates.add(criteriaBuilder.like(root.get("id"), "%" + request.getId() + "%"));
           }

           if (request.getStatus() != null) {
               predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
           }

           if(request.getFrom() != null) {
               predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("orderTime"), request.getFrom()));
           }

           if(request.getTo() != null) {
               predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("orderTime"), request.getTo()));
           }

           return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

    }
}
