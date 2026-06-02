package com.example.productapi.dto;

import jakarta.validation.constraints.NotNull;

public class AddToWishlistRequest {

	@NotNull(message = "Product ID không được để trống")
	private Long productId;

	public AddToWishlistRequest() {
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}