package com.example.productapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.productapi.dto.CreateReviewRequest;
import com.example.productapi.dto.ReviewResponse;
import com.example.productapi.dto.UpdateReviewRequest;
import com.example.productapi.service.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ReviewResponse createReview(@Valid @RequestBody CreateReviewRequest request) {
		return reviewService.createReview(request);
	}

	@GetMapping("/product/{productId}")
	public List<ReviewResponse> getReviewsByProductId(@PathVariable Long productId) {
		return reviewService.getReviewsByProductId(productId);
	}

	@GetMapping("/me")
	public List<ReviewResponse> getMyReviews() {
		return reviewService.getMyReviews();
	}

	@DeleteMapping("/{reviewId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteReview(@PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
	}

	@PutMapping("/{reviewId}")
	public ReviewResponse updateReview(@PathVariable Long reviewId, @Valid @RequestBody UpdateReviewRequest request) {
		return reviewService.updateReview(reviewId, request);
	}
}