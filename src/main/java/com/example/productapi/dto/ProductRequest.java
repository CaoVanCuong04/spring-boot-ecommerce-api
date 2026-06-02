package com.example.productapi.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductRequest {

	@NotBlank(message = "Tên sản phẩm không được để trống")
	@Size(max = 200, message = "Tên sản phẩm không được vượt quá 200 ký tự")
	private String name;

	@Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
	private String description;

	@NotNull(message = "Giá sản phẩm không được để trống")
	@DecimalMin(value = "0.0", inclusive = false, message = "Giá sản phẩm phải lớn hơn 0")
	private BigDecimal price;

	@NotNull(message = "Số lượng sản phẩm không được để trống")
	@Min(value = 0, message = "Số lượng sản phẩm không được âm")
	private Integer quantity;

	@Size(max = 500, message = "Đường dẫn ảnh không được vượt quá 500 ký tự")
	private String imageUrl;

	private Boolean active;

	@NotNull(message = "Danh mục sản phẩm không được để trống")
	private Long categoryId;

	public ProductRequest() {
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public Boolean getActive() {
		return active;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}