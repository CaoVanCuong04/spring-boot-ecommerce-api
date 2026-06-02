package com.example.productapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.productapi.enums.DiscountType;

public class CouponResponse {

	private Long id;
	private String code;
	private String name;
	private DiscountType discountType;
	private BigDecimal discountValue;
	private BigDecimal minOrderAmount;
	private BigDecimal maxDiscountAmount;
	private Integer usageLimit;
	private Integer usedCount;
	private Boolean active;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public CouponResponse() {
	}

	public CouponResponse(Long id, String code, String name, DiscountType discountType, BigDecimal discountValue,
			BigDecimal minOrderAmount, BigDecimal maxDiscountAmount, Integer usageLimit, Integer usedCount,
			Boolean active, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.discountType = discountType;
		this.discountValue = discountValue;
		this.minOrderAmount = minOrderAmount;
		this.maxDiscountAmount = maxDiscountAmount;
		this.usageLimit = usageLimit;
		this.usedCount = usedCount;
		this.active = active;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public BigDecimal getDiscountValue() {
		return discountValue;
	}

	public BigDecimal getMinOrderAmount() {
		return minOrderAmount;
	}

	public BigDecimal getMaxDiscountAmount() {
		return maxDiscountAmount;
	}

	public Integer getUsageLimit() {
		return usageLimit;
	}

	public Integer getUsedCount() {
		return usedCount;
	}

	public Boolean getActive() {
		return active;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}