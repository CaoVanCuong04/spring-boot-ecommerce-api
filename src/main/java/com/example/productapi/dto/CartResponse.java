package com.example.productapi.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {

	private Long id;
	private Long userId;
	private String userEmail;
	private List<CartItemResponse> items;
	private BigDecimal totalAmount;

	public CartResponse() {
	}

	public CartResponse(Long id, Long userId, String userEmail, List<CartItemResponse> items, BigDecimal totalAmount) {
		this.id = id;
		this.userId = userId;
		this.userEmail = userEmail;
		this.items = items;
		this.totalAmount = totalAmount;
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

	public List<CartItemResponse> getItems() {
		return items;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
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

	public void setItems(List<CartItemResponse> items) {
		this.items = items;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
}