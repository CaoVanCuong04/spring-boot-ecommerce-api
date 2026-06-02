package com.example.productapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.productapi.entity.WishlistItem;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

	Optional<WishlistItem> findByWishlistIdAndProductId(Long wishlistId, Long productId);

	boolean existsByWishlistIdAndProductId(Long wishlistId, Long productId);
}