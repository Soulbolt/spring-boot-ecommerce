package com.code.ecommerce.dao;

import com.code.ecommerce.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerEmail(@RequestParam("email") String email, Pageable pageable); // <>
}
