package com.example.productapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WishlistItemResponse {

	private Long id;
	private Long productId;
	private String productName;
	private String imageUrl;
	private BigDecimal price;
	private Boolean productActive;
	private LocalDateTime createdAt;

	public WishlistItemResponse() {
	}

	public WishlistItemResponse(Long id, Long productId, String productName, String imageUrl, BigDecimal price,
			Boolean productActive, LocalDateTime createdAt) {
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.imageUrl = imageUrl;
		this.price = price;
		this.productActive = productActive;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public Long getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Boolean getProductActive() {
		return productActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setProductActive(Boolean productActive) {
		this.productActive = productActive;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}