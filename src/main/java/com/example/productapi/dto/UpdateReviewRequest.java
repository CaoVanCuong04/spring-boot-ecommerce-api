package com.example.productapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateReviewRequest {

	@NotNull(message = "Rating không được để trống")
	@Min(value = 1, message = "Rating phải từ 1 đến 5")
	@Max(value = 5, message = "Rating phải từ 1 đến 5")
	private Integer rating;

	@Size(max = 1000, message = "Bình luận không được vượt quá 1000 ký tự")
	private String comment;

	public UpdateReviewRequest() {
	}

	public Integer getRating() {
		return rating;
	}

	public String getComment() {
		return comment;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}