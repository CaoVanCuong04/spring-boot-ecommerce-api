package com.example.productapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.productapi.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findByProductId(Long productId);

	List<Review> findByUserId(Long userId);

	boolean existsByUserIdAndProductId(Long userId, Long productId);

	Optional<Review> findByUserIdAndProductId(Long userId, Long productId);
}