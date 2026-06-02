package com.example.productapi.dto;

import java.math.BigDecimal;

public class CartItemResponse {

	private Long id;
	private Long productId;
	private String productName;
	private String imageUrl;
	private BigDecimal unitPrice;
	private Integer quantity;
	private BigDecimal totalPrice;

	public CartItemResponse() {
	}

	public CartItemResponse(Long id, Long productId, String productName, String imageUrl, BigDecimal unitPrice,
			Integer quantity, BigDecimal totalPrice) {
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.imageUrl = imageUrl;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
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

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
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

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
}