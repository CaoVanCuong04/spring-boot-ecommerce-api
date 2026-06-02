package com.example.productapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.productapi.dto.AddToWishlistRequest;
import com.example.productapi.dto.WishlistItemResponse;
import com.example.productapi.dto.WishlistResponse;
import com.example.productapi.entity.Product;
import com.example.productapi.entity.User;
import com.example.productapi.entity.Wishlist;
import com.example.productapi.entity.WishlistItem;
import com.example.productapi.exception.ForbiddenException;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.repository.ProductRepository;
import com.example.productapi.repository.UserRepository;
import com.example.productapi.repository.WishlistItemRepository;
import com.example.productapi.repository.WishlistRepository;
import com.example.productapi.security.CurrentUserService;

@Service
public class WishlistService {

	private final WishlistRepository wishlistRepository;
	private final WishlistItemRepository wishlistItemRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final CurrentUserService currentUserService;

	public WishlistService(WishlistRepository wishlistRepository, WishlistItemRepository wishlistItemRepository,
			UserRepository userRepository, ProductRepository productRepository, CurrentUserService currentUserService) {
		this.wishlistRepository = wishlistRepository;
		this.wishlistItemRepository = wishlistItemRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.currentUserService = currentUserService;
	}

	public WishlistResponse getWishlistByUserId(Long userId) {
		Wishlist wishlist = getOrCreateWishlist(userId);
		return mapToResponse(wishlist);
	}

	public WishlistResponse getMyWishlist() {
		Long userId = currentUserService.getCurrentUserId();
		Wishlist wishlist = getOrCreateWishlist(userId);
		return mapToResponse(wishlist);
	}

	public WishlistResponse addToWishlist(AddToWishlistRequest request) {
		Long userId = currentUserService.getCurrentUserId();
		Wishlist wishlist = getOrCreateWishlist(userId);

		Product product = productRepository.findById(request.getProductId()).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id = " + request.getProductId()));

		if (!Boolean.TRUE.equals(product.getActive())) {
			throw new IllegalArgumentException("Sản phẩm hiện không hoạt động");
		}

		if (wishlistItemRepository.existsByWishlistIdAndProductId(wishlist.getId(), product.getId())) {
			throw new IllegalArgumentException("Sản phẩm đã có trong danh sách yêu thích");
		}

		WishlistItem item = new WishlistItem();
		item.setWishlist(wishlist);
		item.setProduct(product);

		wishlistItemRepository.save(item);

		Wishlist updatedWishlist = wishlistRepository.findById(wishlist.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy wishlist"));

		return mapToResponse(updatedWishlist);
	}

	public void deleteWishlistItem(Long wishlistItemId) {
		Long currentUserId = currentUserService.getCurrentUserId();

		WishlistItem wishlistItem = wishlistItemRepository.findById(wishlistItemId).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy wishlist item với id = " + wishlistItemId));

		if (!wishlistItem.getWishlist().getUser().getId().equals(currentUserId)) {
			throw new ForbiddenException("Bạn không có quyền xóa sản phẩm này khỏi danh sách yêu thích");
		}

		wishlistItemRepository.delete(wishlistItem);
	}

	public WishlistResponse clearWishlist(Long userId) {
		Wishlist wishlist = getOrCreateWishlist(userId);
		wishlist.getItems().clear();

		Wishlist savedWishlist = wishlistRepository.save(wishlist);

		return mapToResponse(savedWishlist);
	}

	public WishlistResponse clearMyWishlist() {
		Long userId = currentUserService.getCurrentUserId();

		Wishlist wishlist = getOrCreateWishlist(userId);
		wishlist.getItems().clear();

		Wishlist savedWishlist = wishlistRepository.save(wishlist);

		return mapToResponse(savedWishlist);
	}

	private Wishlist getOrCreateWishlist(Long userId) {
		return wishlistRepository.findByUserId(userId).orElseGet(() -> {
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + userId));

			Wishlist wishlist = new Wishlist();
			wishlist.setUser(user);

			return wishlistRepository.save(wishlist);
		});
	}

	private WishlistResponse mapToResponse(Wishlist wishlist) {
		List<WishlistItemResponse> itemResponses = wishlist.getItems().stream().map(this::mapItemToResponse).toList();

		return new WishlistResponse(wishlist.getId(), wishlist.getUser().getId(), wishlist.getUser().getEmail(),
				itemResponses);
	}

	private WishlistItemResponse mapItemToResponse(WishlistItem item) {
		Product product = item.getProduct();

		return new WishlistItemResponse(item.getId(), product.getId(), product.getName(), product.getImageUrl(),
				product.getPrice(), product.getActive(), item.getCreatedAt());
	}
}