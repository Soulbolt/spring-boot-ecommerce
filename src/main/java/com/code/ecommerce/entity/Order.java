package com.code.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_tracking_number")
    private String orderTrackingNumber;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "status")
    private String status;

    @Column(name = "date_created")
    @CreationTimestamp
    private String dateCreated;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private String lastUpdated;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
