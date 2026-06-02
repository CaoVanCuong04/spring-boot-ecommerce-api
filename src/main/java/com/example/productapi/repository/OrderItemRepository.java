package com.example.productapi.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.productapi.entity.OrderItem;
import com.example.productapi.enums.OrderStatus;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	boolean existsByOrder_User_IdAndProduct_IdAndOrder_StatusIn(Long userId, Long productId,
			Collection<OrderStatus> statuses);
}