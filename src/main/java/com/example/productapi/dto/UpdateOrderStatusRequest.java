package com.example.productapi.dto;

import com.example.productapi.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateOrderStatusRequest {

	@NotNull(message = "Trạng thái đơn hàng không được để trống")
	private OrderStatus status;

	public UpdateOrderStatusRequest() {
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
}