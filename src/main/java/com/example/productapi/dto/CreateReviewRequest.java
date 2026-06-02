package com.example.productapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateReviewRequest {

	@NotNull(message = "Product ID không được để trống")
	private Long productId;

	@NotNull(message = "Số sao không được để trống")
	@Min(value = 1, message = "Số sao tối thiểu là 1")
	@Max(value = 5, message = "Số sao tối đa là 5")
	private Integer rating;

	@Size(max = 1000, message = "Bình luận không được vượt quá 1000 ký tự")
	private String comment;

	public CreateReviewRequest() {
	}

	public Long getProductId() {
		return productId;
	}

	public Integer getRating() {
		return rating;
	}

	public String getComment() {
		return comment;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}