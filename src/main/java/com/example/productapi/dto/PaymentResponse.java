package com.example.productapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.productapi.enums.PaymentMethod;
import com.example.productapi.enums.PaymentStatus;

public class PaymentResponse {

	private Long id;
	private Long orderId;
	private PaymentMethod paymentMethod;
	private PaymentStatus status;
	private BigDecimal amount;
	private String transactionCode;
	private String note;
	private LocalDateTime paidAt;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public PaymentResponse() {
	}

	public PaymentResponse(Long id, Long orderId, PaymentMethod paymentMethod, PaymentStatus status, BigDecimal amount,
			String transactionCode, String note, LocalDateTime paidAt, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.id = id;
		this.orderId = orderId;
		this.paymentMethod = paymentMethod;
		this.status = status;
		this.amount = amount;
		this.transactionCode = transactionCode;
		this.note = note;
		this.paidAt = paidAt;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public String getNote() {
		return note;
	}

	public LocalDateTime getPaidAt() {
		return paidAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}