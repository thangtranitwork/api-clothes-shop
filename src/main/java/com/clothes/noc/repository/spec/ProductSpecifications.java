package com.clothes.noc.repository.spec;

import com.clothes.noc.dto.request.SearchProductRequest;
import com.clothes.noc.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecifications {
    private ProductSpecifications() {}

    public static Specification<Product> multipleFieldsSearch(SearchProductRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Tiện ích để kiểm tra và thêm Predicate
            addPredicateIfNotEmpty(predicates, request.getName(), name ->
                    criteriaBuilder.like(root.get("name"), "%" + name + "%")
            );

            addPredicateIfNotNull(predicates, request.getMinPrice(), minPrice ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice)
            );

            addPredicateIfNotNull(predicates, request.getMaxPrice(), maxPrice ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice)
            );

            // Search by type/subtype
            addJoinPredicate(predicates, request.getSubtype(), "type", "subtype", root, criteriaBuilder);
            if (request.getSubtype() == null) {
                addJoinPredicate(predicates, request.getType(), "type", "type", root, criteriaBuilder);
            }

            // Search by sizes and colors
            addCollectionPredicate(predicates, request.getSizes(), "productVariants", "size", "name", root);
            addCollectionPredicate(predicates, request.getColors(), "productVariants", "color", "name", root);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Tiện ích kiểm tra giá trị chuỗi không rỗng
    private static void addPredicateIfNotEmpty(List<Predicate> predicates, String value, PredicateFunction<String> predicateFunction) {
        if (value != null && !value.isEmpty()) {
            predicates.add(predicateFunction.apply(value));
        }
    }

    // Tiện ích kiểm tra giá trị khác null
    private static <T> void addPredicateIfNotNull(List<Predicate> predicates, T value, PredicateFunction<T> predicateFunction) {
        if (value != null) {
            predicates.add(predicateFunction.apply(value));
        }
    }

    // Tiện ích thêm Predicate cho join (dành cho trường `type` hoặc `subtype`)
    private static void addJoinPredicate(List<Predicate> predicates, String value, String joinPath, String field, Root<?> root, CriteriaBuilder criteriaBuilder) {
        if (value != null) {
            var join = root.join(joinPath);
            predicates.add(criteriaBuilder.equal(join.get(field), value));
        }
    }

    // Tiện ích thêm Predicate cho các collection (dành cho `sizes` hoặc `colors`)
    private static void addCollectionPredicate(List<Predicate> predicates, List<String> values, String joinPath, String subJoinPath, String field, Root<?> root) {
        if (values != null && !values.isEmpty()) {
            var join = root.join(joinPath).join(subJoinPath);
            predicates.add(join.get(field).in(values));
        }
    }

    // Functional interface cho Predicate
    @FunctionalInterface
    private interface PredicateFunction<T> {
        Predicate apply(T value);
    }
}
