package com.example.productapi.dto;

import com.example.productapi.enums.UserRole;

public class AuthResponse {

	private String token;
	private String tokenType = "Bearer";
	private Long id;
	private String fullName;
	private String email;
	private UserRole role;

	public AuthResponse() {
	}

	public AuthResponse(String token, Long id, String fullName, String email, UserRole role) {
		this.token = token;
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public String getTokenType() {
		return tokenType;
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

	public UserRole getRole() {
		return role;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
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

	public void setRole(UserRole role) {
		this.role = role;
	}
}