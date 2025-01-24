package com.clothes.noc.repository.spec;

import com.clothes.noc.dto.request.SearchOrderRequest;
import com.clothes.noc.entity.Order;
import com.clothes.noc.enums.OrderStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecifications {
    private OrderSpecifications() {}
    public static Specification<Order> multipleFieldsSearch(SearchOrderRequest request) {
        try{
            OrderStatus orderStatus = OrderStatus.valueOf(request.getStatus());
            request.setOrderStatus(orderStatus);
        }catch (IllegalArgumentException | NullPointerException e){
            //No need to do something, just avoid orderStatus is null
        }
        return ((root, query, criteriaBuilder) -> {
           List<Predicate> predicates = new ArrayList<>();
            if(request.getUserId() != null && !request.getUserId().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.join("user").get("id"), request.getUserId()));
            }

           if(request.getId() != null && !request.getId().isEmpty()) {
               predicates.add(criteriaBuilder.like(root.get("id"), "%" + request.getId() + "%"));
           }

           if (request.getOrderStatus() != null) {
               predicates.add(criteriaBuilder.equal(root.get("status"), request.getOrderStatus()));
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
