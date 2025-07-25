package com.codeunsocial.order_service.repository;

import com.codeunsocial.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
