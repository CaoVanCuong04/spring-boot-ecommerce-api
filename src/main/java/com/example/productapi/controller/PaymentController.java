package com.example.productapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.productapi.dto.CreatePaymentRequest;
import com.example.productapi.dto.PaymentResponse;
import com.example.productapi.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PaymentResponse createPayment(@Valid @RequestBody CreatePaymentRequest request) {
		return paymentService.createPayment(request);
	}

	@GetMapping("/{paymentId}")
	public PaymentResponse getPaymentById(@PathVariable Long paymentId) {
		return paymentService.getPaymentById(paymentId);
	}

	@GetMapping("/order/{orderId}")
	public PaymentResponse getPaymentByOrderId(@PathVariable Long orderId) {
		return paymentService.getPaymentByOrderId(orderId);
	}
}