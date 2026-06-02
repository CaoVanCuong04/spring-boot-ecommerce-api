package com.example.productapi.dto;

import java.time.LocalDateTime;

import com.example.productapi.enums.UserRole;

public class UserResponse {

	private Long id;
	private String fullName;
	private String email;
	private String phone;
	private UserRole role;
	private Boolean active;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public UserResponse() {
	}

	public UserResponse(Long id, String fullName, String email, String phone, UserRole role, Boolean active,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.role = role;
		this.active = active;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public UserRole getRole() {
		return role;
	}

	public Boolean getActive() {
		return active;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}