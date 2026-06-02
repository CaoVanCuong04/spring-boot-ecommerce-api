package com.example.productapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.productapi.dto.CreateOrderRequest;
import com.example.productapi.dto.OrderResponse;
import com.example.productapi.dto.UpdateOrderStatusRequest;
import com.example.productapi.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
		return orderService.createOrder(request);
	}

	@GetMapping("/my-orders")
	public List<OrderResponse> getMyOrders() {
		return orderService.getMyOrders();
	}

	@GetMapping("/{orderId}")
	public OrderResponse getOrderById(@PathVariable Long orderId) {
		return orderService.getOrderById(orderId);
	}

	@PutMapping("/{orderId}/status")
	public OrderResponse updateOrderStatus(@PathVariable Long orderId,
			@Valid @RequestBody UpdateOrderStatusRequest request) {
		return orderService.updateOrderStatus(orderId, request);
	}

	@PutMapping("/{orderId}/cancel")
	public OrderResponse cancelOrder(@PathVariable Long orderId) {
		return orderService.cancelOrder(orderId);
	}
}