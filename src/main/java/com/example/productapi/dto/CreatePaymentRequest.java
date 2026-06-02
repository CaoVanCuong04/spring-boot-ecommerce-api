package com.example.productapi.dto;

import com.example.productapi.enums.PaymentMethod;

import jakarta.validation.constraints.NotNull;

public class CreatePaymentRequest {

	@NotNull(message = "Order ID không được để trống")
	private Long orderId;

	@NotNull(message = "Phương thức thanh toán không được để trống")
	private PaymentMethod paymentMethod;

	// SUCCESS hoặc FAILED để giả lập kết quả thanh toán
	private String simulateResult;

	public CreatePaymentRequest() {
	}

	public Long getOrderId() {
		return orderId;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public String getSimulateResult() {
		return simulateResult;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public void setSimulateResult(String simulateResult) {
		this.simulateResult = simulateResult;
	}
}