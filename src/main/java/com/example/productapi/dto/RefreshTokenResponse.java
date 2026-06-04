package com.example.productapi.dto;

public class RefreshTokenResponse {

	private String accessToken;
	private String refreshToken;
	private String tokenType = "Bearer";

	public RefreshTokenResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public String getTokenType() {
		return tokenType;
	}
}