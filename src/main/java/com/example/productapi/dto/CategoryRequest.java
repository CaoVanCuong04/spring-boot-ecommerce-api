package com.example.productapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequest {

	@NotBlank(message = "Tên danh mục không được để trống")
	@Size(max = 100, message = "Tên danh mục không được vượt quá 100 ký tự")
	private String name;

	@Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
	private String description;

	private Boolean active;

	public CategoryRequest() {
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}