package com.example.productapi.dto;

import java.util.List;

public class WishlistResponse {

	private Long id;
	private Long userId;
	private String userEmail;
	private List<WishlistItemResponse> items;

	public WishlistResponse() {
	}

	public WishlistResponse(Long id, Long userId, String userEmail, List<WishlistItemResponse> items) {
		this.id = id;
		this.userId = userId;
		this.userEmail = userEmail;
		this.items = items;
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

	public List<WishlistItemResponse> getItems() {
		return items;
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

	public void setItems(List<WishlistItemResponse> items) {
		this.items = items;
	}
}