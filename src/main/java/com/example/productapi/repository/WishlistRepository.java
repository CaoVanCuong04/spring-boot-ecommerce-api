package com.example.productapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.productapi.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

	Optional<Wishlist> findByUserId(Long userId);
}