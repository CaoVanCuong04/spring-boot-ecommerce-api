package com.example.productapi.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.productapi.entity.User;
import com.example.productapi.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user với email = " + email));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				Boolean.TRUE.equals(user.getActive()), true, true, true,
				List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
	}
}