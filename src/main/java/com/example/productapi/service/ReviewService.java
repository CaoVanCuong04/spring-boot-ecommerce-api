package com.example.productapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.productapi.dto.CreateReviewRequest;
import com.example.productapi.dto.ReviewResponse;
import com.example.productapi.dto.UpdateReviewRequest;
import com.example.productapi.entity.Product;
import com.example.productapi.entity.Review;
import com.example.productapi.entity.User;
import com.example.productapi.enums.OrderStatus;
import com.example.productapi.enums.UserRole;
import com.example.productapi.exception.ForbiddenException;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.repository.OrderItemRepository;
import com.example.productapi.repository.ProductRepository;
import com.example.productapi.repository.ReviewRepository;
import com.example.productapi.repository.UserRepository;
import com.example.productapi.security.CurrentUserService;

@Service
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final CurrentUserService currentUserService;
	private final OrderItemRepository orderItemRepository;

	public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository,
			ProductRepository productRepository, CurrentUserService currentUserService,
			OrderItemRepository orderItemRepository) {
		this.reviewRepository = reviewRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.currentUserService = currentUserService;
		this.orderItemRepository = orderItemRepository;
	}

	public ReviewResponse createReview(CreateReviewRequest request) {
		User user = currentUserService.getCurrentUser();

		Product product = productRepository.findById(request.getProductId()).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id = " + request.getProductId()));

		if (!Boolean.TRUE.equals(product.getActive())) {
			throw new IllegalArgumentException("Sản phẩm hiện không hoạt động");
		}

		List<OrderStatus> validStatuses = List.of(OrderStatus.CONFIRMED, OrderStatus.SHIPPING, OrderStatus.COMPLETED);

		boolean hasPurchased = orderItemRepository.existsByOrder_User_IdAndProduct_IdAndOrder_StatusIn(user.getId(),
				product.getId(), validStatuses);

		if (!hasPurchased) {
			throw new IllegalArgumentException("Bạn chỉ được đánh giá sản phẩm đã mua");
		}

		if (reviewRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {
			throw new IllegalArgumentException("Bạn đã đánh giá sản phẩm này rồi");
		}

		Review review = new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setRating(request.getRating());
		review.setComment(request.getComment());

		Review savedReview = reviewRepository.save(review);

		return mapToResponse(savedReview);
	}

	public List<ReviewResponse> getReviewsByProductId(Long productId) {
		productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id = " + productId));

		return reviewRepository.findByProductId(productId).stream().map(this::mapToResponse).toList();
	}

	public List<ReviewResponse> getMyReviews() {
		Long userId = currentUserService.getCurrentUserId();

		return reviewRepository.findByUserId(userId).stream().map(this::mapToResponse).toList();
	}

	public List<ReviewResponse> getReviewsByUserId(Long userId) {
		userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + userId));

		return reviewRepository.findByUserId(userId).stream().map(this::mapToResponse).toList();
	}

	public ReviewResponse updateReview(Long reviewId, UpdateReviewRequest request) {
		User currentUser = currentUserService.getCurrentUser();

		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy review với id = " + reviewId));

		if (!review.getUser().getId().equals(currentUser.getId())) {
			throw new ForbiddenException("Bạn không có quyền sửa đánh giá này");
		}

		review.setRating(request.getRating());
		review.setComment(request.getComment());

		Review savedReview = reviewRepository.save(review);

		return mapToResponse(savedReview);
	}

	public void deleteReview(Long reviewId) {
		User currentUser = currentUserService.getCurrentUser();

		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy review với id = " + reviewId));

		boolean isOwner = review.getUser().getId().equals(currentUser.getId());
		boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

		if (!isOwner && !isAdmin) {
			throw new ForbiddenException("Bạn không có quyền xóa đánh giá này");
		}

		reviewRepository.delete(review);
	}

	private ReviewResponse mapToResponse(Review review) {
		return new ReviewResponse(review.getId(), review.getUser().getId(), review.getUser().getEmail(),
				review.getUser().getFullName(), review.getProduct().getId(), review.getProduct().getName(),
				review.getRating(), review.getComment(), review.getCreatedAt(), review.getUpdatedAt());
	}
}