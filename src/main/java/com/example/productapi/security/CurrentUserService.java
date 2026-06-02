package com.example.productapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.productapi.entity.User;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.repository.UserRepository;

@Service
public class CurrentUserService {

	private final UserRepository userRepository;

	public CurrentUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || authentication.getName() == null) {
			throw new IllegalArgumentException("Người dùng chưa đăng nhập");
		}

		String email = authentication.getName();

		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với email = " + email));
	}

	public Long getCurrentUserId() {
		return getCurrentUser().getId();
	}
}