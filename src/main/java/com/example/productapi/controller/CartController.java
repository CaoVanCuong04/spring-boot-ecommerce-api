package com.example.productapi.controller;

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

import com.example.productapi.dto.AddToCartRequest;
import com.example.productapi.dto.CartResponse;
import com.example.productapi.dto.UpdateCartItemRequest;
import com.example.productapi.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping("/me")
	public CartResponse getMyCart() {
		return cartService.getMyCart();
	}

	@PostMapping("/items")
	@ResponseStatus(HttpStatus.CREATED)
	public CartResponse addToCart(@Valid @RequestBody AddToCartRequest request) {
		return cartService.addToCart(request);
	}

	@PutMapping("/items/{cartItemId}")
	public CartResponse updateCartItem(@PathVariable Long cartItemId,
			@Valid @RequestBody UpdateCartItemRequest request) {
		return cartService.updateCartItem(cartItemId, request);
	}

	@DeleteMapping("/items/{cartItemId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCartItem(@PathVariable Long cartItemId) {
		cartService.deleteCartItem(cartItemId);
	}

	@DeleteMapping("/me/clear")
	public CartResponse clearMyCart() {
		return cartService.clearMyCart();
	}
}