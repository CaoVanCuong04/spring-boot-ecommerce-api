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

import com.example.productapi.dto.CouponRequest;
import com.example.productapi.dto.CouponResponse;
import com.example.productapi.service.CouponService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

	private final CouponService couponService;

	public CouponController(CouponService couponService) {
		this.couponService = couponService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CouponResponse createCoupon(@Valid @RequestBody CouponRequest request) {
		return couponService.createCoupon(request);
	}

	@GetMapping
	public List<CouponResponse> getAllCoupons() {
		return couponService.getAllCoupons();
	}

	@GetMapping("/{id}")
	public CouponResponse getCouponById(@PathVariable Long id) {
		return couponService.getCouponById(id);
	}

	@GetMapping("/code/{code}")
	public CouponResponse getCouponByCode(@PathVariable String code) {
		return couponService.getCouponByCode(code);
	}

	@PutMapping("/{id}")
	public CouponResponse updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponRequest request) {
		return couponService.updateCoupon(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCoupon(@PathVariable Long id) {
		couponService.deleteCoupon(id);
	}
}