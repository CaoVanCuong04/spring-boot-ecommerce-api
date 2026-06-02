package com.example.productapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.productapi.enums.OrderStatus;

public class OrderResponse {

	private Long id;
	private Long userId;
	private String userEmail;
	private BigDecimal totalAmount;
	private OrderStatus status;
	private String shippingAddress;
	private String receiverName;
	private String receiverPhone;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<OrderItemResponse> items;
	private String couponCode;
	private BigDecimal discountAmount;
	private BigDecimal finalAmount;

	public OrderResponse() {
	}

	public OrderResponse(Long id, Long userId, String userEmail, BigDecimal totalAmount, String couponCode,
			BigDecimal discountAmount, BigDecimal finalAmount, OrderStatus status, String shippingAddress,
			String receiverName, String receiverPhone, LocalDateTime createdAt, LocalDateTime updatedAt,
			List<OrderItemResponse> items) {
		this.id = id;
		this.userId = userId;
		this.userEmail = userEmail;
		this.totalAmount = totalAmount;
		this.couponCode = couponCode;
		this.discountAmount = discountAmount;
		this.finalAmount = finalAmount;
		this.status = status;
		this.shippingAddress = shippingAddress;
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.items = items;
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public BigDecimal getFinalAmount() {
		return finalAmount;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public List<OrderItemResponse> getItems() {
		return items;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setItems(List<OrderItemResponse> items) {
		this.items = items;
	}
}