package com.clothes.noc.repository;

import com.clothes.noc.entity.Order;
import com.clothes.noc.enums.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
    @Query(value = "SELECT HOUR(o.order_time) as hour, SUM(oi.price * oi.quantity) as total " +
            "FROM `order` o JOIN order_item oi ON o.id = oi.order_id " +
            "WHERE DATE(o.order_time) = :date AND o.status = 'SUCCESS'" +
            "GROUP BY HOUR(o.order_time) " +
            "ORDER BY hour",
            nativeQuery = true)
    List<Object[]> summaryByDay(LocalDate date);
    @Query(value = "SELECT DAYOFWEEK(o.order_time) as day, SUM(oi.price * oi.quantity) as total " +
            "FROM `order` o JOIN order_item oi ON o.id = oi.order_id " +
            "WHERE YEAR(o.order_time) = :year " +
            "AND WEEK(o.order_time) = :week AND o.status = 'SUCCESS'" +
            "GROUP BY day " +
            "ORDER BY day",
            nativeQuery = true)
    List<Object[]> summaryByWeek(int year, int week);

    @Query(value = "SELECT DAY(o.order_time) as day, SUM(oi.price * oi.quantity) as total " +
            "FROM `order` o JOIN order_item oi ON o.id = oi.order_id " +
            "WHERE YEAR(o.order_time) = :year " +
            "AND MONTH(o.order_time) = :month AND o.status = 'SUCCESS'" +
            "GROUP BY day " +
            "ORDER BY day",
            nativeQuery = true)
    List<Object[]> summaryByMonth(int year, int month);

    @Query(value = "SELECT MONTH(o.order_time) as month, SUM(oi.price * oi.quantity) as total " +
            "FROM `order` o JOIN order_item oi ON o.id = oi.order_id " +
            "WHERE YEAR(o.order_time) = :year AND o.status = 'SUCCESS'" +
            "GROUP BY month " +
            "ORDER BY month",
            nativeQuery = true)
    List<Object[]> summaryByYear(int year);

    @Query(value = """
SELECT pt.subtype, IFNULL(SUM(oi.price * oi.quantity), 0) AS total
FROM
    product_type pt
LEFT JOIN
    product p ON pt.id = p.product_type_id
LEFT JOIN
    product_variant pv ON p.id = pv.product_id
LEFT JOIN
    order_item oi ON pv.id = oi.product_variant_id
LEFT JOIN
    `order` o ON oi.order_id = o.id AND o.status = 'SUCCESS'
GROUP BY
    pt.subtype;
""", nativeQuery = true)
    List<Object[]> summaryByCategory();
    @Query("SELECT o.status FROM Order o WHERE o.id=?1")
    Optional<OrderStatus> getOrderStatus(String orderId);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = ?2 WHERE o.id = ?1")
    int updateOrderStatus(String orderId, OrderStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = ?2, o.payment.payTime = ?3 WHERE o.id = ?1")
    int updateOrderStatusAndPayTime(String orderId, OrderStatus status, LocalDateTime time);
}
