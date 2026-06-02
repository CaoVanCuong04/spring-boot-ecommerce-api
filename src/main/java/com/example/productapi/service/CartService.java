package com.example.productapi.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.productapi.dto.AddToCartRequest;
import com.example.productapi.dto.CartItemResponse;
import com.example.productapi.dto.CartResponse;
import com.example.productapi.dto.UpdateCartItemRequest;
import com.example.productapi.entity.Cart;
import com.example.productapi.entity.CartItem;
import com.example.productapi.entity.Product;
import com.example.productapi.entity.User;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.repository.CartItemRepository;
import com.example.productapi.repository.CartRepository;
import com.example.productapi.repository.ProductRepository;
import com.example.productapi.repository.UserRepository;
import com.example.productapi.security.CurrentUserService;

@Service
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final CurrentUserService currentUserService;

	public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
			UserRepository userRepository, ProductRepository productRepository, CurrentUserService currentUserService) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.currentUserService = currentUserService;
	}

	public CartResponse getCartByUserId(Long userId) {
		Cart cart = getOrCreateCart(userId);
		return mapToResponse(cart);
	}

	public CartResponse addToCart(AddToCartRequest request) {
		Long userId = currentUserService.getCurrentUserId();
		Cart cart = getOrCreateCart(userId);

		Product product = productRepository.findById(request.getProductId()).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id = " + request.getProductId()));

		if (!Boolean.TRUE.equals(product.getActive())) {
			throw new IllegalArgumentException("Sản phẩm hiện không hoạt động");
		}

		if (product.getQuantity() < request.getQuantity()) {
			throw new IllegalArgumentException("Số lượng sản phẩm trong kho không đủ");
		}

		CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId()).orElse(null);

		if (cartItem == null) {
			cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cartItem.setQuantity(request.getQuantity());
			cartItem.setUnitPrice(product.getPrice());
		} else {
			int newQuantity = cartItem.getQuantity() + request.getQuantity();

			if (product.getQuantity() < newQuantity) {
				throw new IllegalArgumentException("Số lượng sản phẩm trong kho không đủ");
			}

			cartItem.setQuantity(newQuantity);
		}

		cartItemRepository.save(cartItem);

		Cart updatedCart = cartRepository.findById(cart.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));

		return mapToResponse(updatedCart);
	}

	public CartResponse updateCartItem(Long cartItemId, UpdateCartItemRequest request) {
		Long currentUserId = currentUserService.getCurrentUserId();

		CartItem cartItem = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cart item với id = " + cartItemId));

		if (!cartItem.getCart().getUser().getId().equals(currentUserId)) {
			throw new IllegalArgumentException("Bạn không có quyền sửa sản phẩm này trong giỏ hàng");
		}

		Product product = cartItem.getProduct();

		if (product.getQuantity() < request.getQuantity()) {
			throw new IllegalArgumentException("Số lượng sản phẩm trong kho không đủ");
		}

		cartItem.setQuantity(request.getQuantity());
		cartItemRepository.save(cartItem);

		Cart cart = cartItem.getCart();

		return mapToResponse(cart);
	}

	public void deleteCartItem(Long cartItemId) {
		Long currentUserId = currentUserService.getCurrentUserId();

		CartItem cartItem = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cart item với id = " + cartItemId));

		if (!cartItem.getCart().getUser().getId().equals(currentUserId)) {
			throw new IllegalArgumentException("Bạn không có quyền xóa sản phẩm này khỏi giỏ hàng");
		}

		cartItemRepository.delete(cartItem);
	}

	public CartResponse clearMyCart() {
		Long userId = currentUserService.getCurrentUserId();

		Cart cart = getOrCreateCart(userId);
		cart.getItems().clear();

		Cart savedCart = cartRepository.save(cart);

		return mapToResponse(savedCart);
	}

	private Cart getOrCreateCart(Long userId) {
		return cartRepository.findByUserId(userId).orElseGet(() -> {
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + userId));

			Cart cart = new Cart();
			cart.setUser(user);

			return cartRepository.save(cart);
		});
	}

	private CartResponse mapToResponse(Cart cart) {
		List<CartItemResponse> itemResponses = cart.getItems().stream().map(this::mapItemToResponse).toList();

		BigDecimal totalAmount = itemResponses.stream().map(CartItemResponse::getTotalPrice).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return new CartResponse(cart.getId(), cart.getUser().getId(), cart.getUser().getEmail(), itemResponses,
				totalAmount);
	}

	private CartItemResponse mapItemToResponse(CartItem item) {
		BigDecimal totalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

		return new CartItemResponse(item.getId(), item.getProduct().getId(), item.getProduct().getName(),
				item.getProduct().getImageUrl(), item.getUnitPrice(), item.getQuantity(), totalPrice);
	}

	public CartResponse getMyCart() {
		Long userId = currentUserService.getCurrentUserId();
		Cart cart = getOrCreateCart(userId);
		return mapToResponse(cart);
	}
}