package com.example.productapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.productapi.dto.CouponRequest;
import com.example.productapi.dto.CouponResponse;
import com.example.productapi.entity.Coupon;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.repository.CouponRepository;

@Service
public class CouponService {

	private final CouponRepository couponRepository;

	public CouponService(CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}

	public CouponResponse createCoupon(CouponRequest request) {
		String code = request.getCode().trim().toUpperCase();

		if (couponRepository.existsByCode(code)) {
			throw new IllegalArgumentException("Mã giảm giá đã tồn tại");
		}

		Coupon coupon = new Coupon();
		coupon.setCode(code);
		coupon.setName(request.getName());
		coupon.setDiscountType(request.getDiscountType());
		coupon.setDiscountValue(request.getDiscountValue());
		coupon.setMinOrderAmount(request.getMinOrderAmount());
		coupon.setMaxDiscountAmount(request.getMaxDiscountAmount());
		coupon.setUsageLimit(request.getUsageLimit());
		coupon.setUsedCount(0);
		coupon.setActive(request.getActive() == null ? true : request.getActive());
		coupon.setStartDate(request.getStartDate());
		coupon.setEndDate(request.getEndDate());

		Coupon savedCoupon = couponRepository.save(coupon);

		return mapToResponse(savedCoupon);
	}

	public List<CouponResponse> getAllCoupons() {
		return couponRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	public CouponResponse getCouponById(Long id) {
		Coupon coupon = findCouponById(id);
		return mapToResponse(coupon);
	}

	public CouponResponse getCouponByCode(String code) {
		Coupon coupon = couponRepository.findByCode(code.trim().toUpperCase())
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã giảm giá: " + code));

		return mapToResponse(coupon);
	}

	public CouponResponse updateCoupon(Long id, CouponRequest request) {
		Coupon coupon = findCouponById(id);
		String code = request.getCode().trim().toUpperCase();

		if (!coupon.getCode().equals(code) && couponRepository.existsByCode(code)) {
			throw new IllegalArgumentException("Mã giảm giá đã tồn tại");
		}

		coupon.setCode(code);
		coupon.setName(request.getName());
		coupon.setDiscountType(request.getDiscountType());
		coupon.setDiscountValue(request.getDiscountValue());
		coupon.setMinOrderAmount(request.getMinOrderAmount());
		coupon.setMaxDiscountAmount(request.getMaxDiscountAmount());
		coupon.setUsageLimit(request.getUsageLimit());
		coupon.setActive(request.getActive() == null ? coupon.getActive() : request.getActive());
		coupon.setStartDate(request.getStartDate());
		coupon.setEndDate(request.getEndDate());

		Coupon savedCoupon = couponRepository.save(coupon);

		return mapToResponse(savedCoupon);
	}

	public void deleteCoupon(Long id) {
		Coupon coupon = findCouponById(id);
		couponRepository.delete(coupon);
	}

	private Coupon findCouponById(Long id) {
		return couponRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã giảm giá với id = " + id));
	}

	private CouponResponse mapToResponse(Coupon coupon) {
		return new CouponResponse(coupon.getId(), coupon.getCode(), coupon.getName(), coupon.getDiscountType(),
				coupon.getDiscountValue(), coupon.getMinOrderAmount(), coupon.getMaxDiscountAmount(),
				coupon.getUsageLimit(), coupon.getUsedCount(), coupon.getActive(), coupon.getStartDate(),
				coupon.getEndDate(), coupon.getCreatedAt(), coupon.getUpdatedAt());
	}
}