package com.example.productapi.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productapi.entity.RefreshToken;
import com.example.productapi.entity.User;
import com.example.productapi.exception.UnauthorizedException;
import com.example.productapi.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${app.jwt.refresh-expiration-ms}")
	private Long refreshTokenExpirationMs;

	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
	}

	public RefreshToken createRefreshToken(User user) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUser(user);
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpirationMs / 1000));
		refreshToken.setRevoked(false);

		return refreshTokenRepository.save(refreshToken);
	}

	public RefreshToken verifyRefreshToken(String token) {
		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new UnauthorizedException("Refresh token không hợp lệ"));

		if (refreshToken.isRevoked()) {
			throw new UnauthorizedException("Refresh token đã bị thu hồi");
		}

		if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			throw new UnauthorizedException("Refresh token đã hết hạn");
		}

		return refreshToken;
	}

	@Transactional
	public void revokeByUser(User user) {
		refreshTokenRepository.deleteByUser(user);
	}

	public void revokeToken(String token) {
		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new UnauthorizedException("Refresh token không hợp lệ"));

		refreshToken.setRevoked(true);
		refreshTokenRepository.save(refreshToken);
	}
}