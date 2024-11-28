package com.clothes.noc.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false)
    @Builder.Default
    Date orderTime = new Date();
    @Column(columnDefinition = "char(12)", nullable = false)
    String phoneNumber;
    @Column(nullable = false)
    String address;
    @Column(nullable = false)
    String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    OrderStatus status = OrderStatus.NEW;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<OrderItem> items;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    Payment payment;
}
