package com.example.productapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.productapi.enums.DiscountType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CouponRequest {

	@NotBlank(message = "Mã giảm giá không được để trống")
	@Size(max = 50, message = "Mã giảm giá không được vượt quá 50 ký tự")
	private String code;

	@NotBlank(message = "Tên mã giảm giá không được để trống")
	@Size(max = 255, message = "Tên mã giảm giá không được vượt quá 255 ký tự")
	private String name;

	@NotNull(message = "Loại giảm giá không được để trống")
	private DiscountType discountType;

	@NotNull(message = "Giá trị giảm giá không được để trống")
	@DecimalMin(value = "0.0", inclusive = false, message = "Giá trị giảm giá phải lớn hơn 0")
	private BigDecimal discountValue;

	@DecimalMin(value = "0.0", message = "Giá trị đơn tối thiểu không được âm")
	private BigDecimal minOrderAmount;

	@DecimalMin(value = "0.0", message = "Mức giảm tối đa không được âm")
	private BigDecimal maxDiscountAmount;

	@NotNull(message = "Giới hạn lượt dùng không được để trống")
	@Min(value = 1, message = "Giới hạn lượt dùng phải lớn hơn 0")
	private Integer usageLimit;

	private Boolean active;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	public CouponRequest() {
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

	public Boolean getActive() {
		return active;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}

	public void setMinOrderAmount(BigDecimal minOrderAmount) {
		this.minOrderAmount = minOrderAmount;
	}

	public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
		this.maxDiscountAmount = maxDiscountAmount;
	}

	public void setUsageLimit(Integer usageLimit) {
		this.usageLimit = usageLimit;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
}