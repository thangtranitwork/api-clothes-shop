package com.clothes.noc.repository.spec;

import com.clothes.noc.dto.request.SearchProductRequest;
import com.clothes.noc.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecifications {
    public static Specification<Product> multipleFieldsSearch(SearchProductRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search by name
            if (request.getName() != null && !request.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%"));
            }

            // Search by price range
            if (request.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), request.getMinPrice()));
            }

            if (request.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
            }

            // Search by product type and subtype
            if (request.getSubtype() != null) {
                var typeJoin = root.join("type");
                predicates.add(criteriaBuilder.equal(typeJoin.get("subtype"), request.getSubtype()));
            } else if (request.getType() != null) {
                var typeJoin = root.join("type");
                predicates.add(criteriaBuilder.equal(typeJoin.get("type"), request.getType()));
            }

            // Search by sizes (name of Size)
            if (request.getSizes() != null && !request.getSizes().isEmpty()) {
                var productVariantsJoin = root.join("productVariants");
                var sizeJoin = productVariantsJoin.join("size");
                predicates.add(sizeJoin.get("name").in(request.getSizes()));
            }

            // Search by colors (name of Color)
            if (request.getColors() != null && !request.getColors().isEmpty()) {
                var productVariantsJoin = root.join("productVariants");
                var colorJoin = productVariantsJoin.join("color");
                predicates.add(colorJoin.get("name").in(request.getColors()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
