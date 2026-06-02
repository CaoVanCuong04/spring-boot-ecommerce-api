package com.example.productapi.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productapi.dto.CreatePaymentRequest;
import com.example.productapi.dto.PaymentResponse;
import com.example.productapi.entity.Order;
import com.example.productapi.entity.Payment;
import com.example.productapi.entity.User;
import com.example.productapi.enums.OrderStatus;
import com.example.productapi.enums.PaymentStatus;
import com.example.productapi.exception.ForbiddenException;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.repository.OrderRepository;
import com.example.productapi.repository.PaymentRepository;
import com.example.productapi.security.CurrentUserService;

@Service
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	private final CurrentUserService currentUserService;

	public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository,
			CurrentUserService currentUserService) {
		this.paymentRepository = paymentRepository;
		this.orderRepository = orderRepository;
		this.currentUserService = currentUserService;
	}

	@Transactional
	public PaymentResponse createPayment(CreatePaymentRequest request) {
		User currentUser = currentUserService.getCurrentUser();

		Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id = " + request.getOrderId()));

		if (!order.getUser().getId().equals(currentUser.getId())) {
			throw new ForbiddenException("Bạn không có quyền thanh toán đơn hàng này");
		}

		if (paymentRepository.existsByOrderId(order.getId())) {
			throw new IllegalArgumentException("Đơn hàng này đã có giao dịch thanh toán");
		}

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setPaymentMethod(request.getPaymentMethod());
		payment.setAmount(order.getFinalAmount());

		String simulateResult = request.getSimulateResult();

		if ("FAILED".equalsIgnoreCase(simulateResult)) {
			payment.setStatus(PaymentStatus.FAILED);
			payment.setNote("Thanh toán thất bại do giả lập");
		} else {
			payment.setStatus(PaymentStatus.SUCCESS);
			payment.setPaidAt(LocalDateTime.now());
			payment.setTransactionCode("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			payment.setNote("Thanh toán thành công");

			order.setStatus(OrderStatus.CONFIRMED);
			orderRepository.save(order);
		}

		Payment savedPayment = paymentRepository.save(payment);

		return mapToResponse(savedPayment);
	}

	public PaymentResponse getPaymentById(Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy payment với id = " + paymentId));

		return mapToResponse(payment);
	}

	public PaymentResponse getPaymentByOrderId(Long orderId) {
		User currentUser = currentUserService.getCurrentUser();

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id = " + orderId));

		if (!order.getUser().getId().equals(currentUser.getId())) {
			throw new ForbiddenException("Bạn không có quyền xem thanh toán của đơn hàng này");
		}

		Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy thanh toán cho đơn hàng id = " + orderId));

		return mapToResponse(payment);
	}

	private PaymentResponse mapToResponse(Payment payment) {
		return new PaymentResponse(payment.getId(), payment.getOrder().getId(), payment.getPaymentMethod(),
				payment.getStatus(), payment.getAmount(), payment.getTransactionCode(), payment.getNote(),
				payment.getPaidAt(), payment.getCreatedAt(), payment.getUpdatedAt());
	}
}