package com.example.productapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.productapi.dto.AuthResponse;
import com.example.productapi.dto.LoginRequest;
import com.example.productapi.dto.LogoutRequest;
import com.example.productapi.dto.RefreshTokenRequest;
import com.example.productapi.dto.RefreshTokenResponse;
import com.example.productapi.dto.RegisterRequest;
import com.example.productapi.dto.UserResponse;
import com.example.productapi.entity.RefreshToken;
import com.example.productapi.entity.User;
import com.example.productapi.enums.UserRole;
import com.example.productapi.exception.UnauthorizedException;
import com.example.productapi.repository.UserRepository;
import com.example.productapi.security.JwtService;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
			RefreshTokenService refreshTokenService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.refreshTokenService = refreshTokenService;
	}

	public UserResponse register(RegisterRequest request) {
		String email = request.getEmail().trim().toLowerCase();

		if (userRepository.existsByEmail(email)) {
			throw new IllegalArgumentException("Email đã tồn tại");
		}

		User user = new User();
		user.setFullName(request.getFullName());
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setPhone(request.getPhone());

		// Khóa role khi đăng ký public
		user.setRole(UserRole.USER);

		user.setActive(true);

		User savedUser = userRepository.save(user);

		return mapToResponse(savedUser);
	}

	public AuthResponse login(LoginRequest request) {
		String email = request.getEmail().trim().toLowerCase();

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UnauthorizedException("Email hoặc mật khẩu không đúng"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new UnauthorizedException("Email hoặc mật khẩu không đúng");
		}

		if (!Boolean.TRUE.equals(user.getActive())) {
			throw new IllegalArgumentException("Tài khoản đã bị khóa");
		}

		String token = jwtService.generateToken(user);

		String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

		return new AuthResponse(token, refreshToken, user.getId(), user.getFullName(), user.getEmail(), user.getRole());
	}

	private UserResponse mapToResponse(User user) {
		return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getPhone(), user.getRole(),
				user.getActive(), user.getCreatedAt(), user.getUpdatedAt());
	}

	public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
		RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());

		User user = refreshToken.getUser();

		String newAccessToken = jwtService.generateToken(user);

		return new RefreshTokenResponse(newAccessToken, refreshToken.getToken());
	}

	public void logout(LogoutRequest request) {
		refreshTokenService.revokeToken(request.getRefreshToken());
	}
}