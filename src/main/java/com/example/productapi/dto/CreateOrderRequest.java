package com.example.productapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateOrderRequest {

	@NotBlank(message = "Địa chỉ giao hàng không được để trống")
	@Size(max = 255, message = "Địa chỉ giao hàng không được vượt quá 255 ký tự")
	private String shippingAddress;

	@NotBlank(message = "Tên người nhận không được để trống")
	@Size(max = 150, message = "Tên người nhận không được vượt quá 150 ký tự")
	private String receiverName;

	@NotBlank(message = "Số điện thoại người nhận không được để trống")
	@Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
	private String receiverPhone;

	@Size(max = 50, message = "Mã giảm giá không được vượt quá 50 ký tự")
	private String couponCode;

	public CreateOrderRequest() {
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
}