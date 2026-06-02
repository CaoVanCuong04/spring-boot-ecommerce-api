package com.example.productapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.productapi.dto.AddToWishlistRequest;
import com.example.productapi.dto.WishlistResponse;
import com.example.productapi.service.WishlistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

	private final WishlistService wishlistService;

	public WishlistController(WishlistService wishlistService) {
		this.wishlistService = wishlistService;
	}

	@GetMapping("/me")
	public WishlistResponse getMyWishlist() {
		return wishlistService.getMyWishlist();
	}

	@PostMapping("/items")
	@ResponseStatus(HttpStatus.CREATED)
	public WishlistResponse addToWishlist(@Valid @RequestBody AddToWishlistRequest request) {
		return wishlistService.addToWishlist(request);
	}

	@DeleteMapping("/items/{wishlistItemId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteWishlistItem(@PathVariable Long wishlistItemId) {
		wishlistService.deleteWishlistItem(wishlistItemId);
	}

	@DeleteMapping("/me/clear")
	public WishlistResponse clearMyWishlist() {
		return wishlistService.clearMyWishlist();
	}
}