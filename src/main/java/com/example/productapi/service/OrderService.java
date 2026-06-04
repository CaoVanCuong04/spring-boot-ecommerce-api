package com.example.productapi.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productapi.dto.CreateOrderRequest;
import com.example.productapi.dto.OrderItemResponse;
import com.example.productapi.dto.OrderResponse;
import com.example.productapi.dto.UpdateOrderStatusRequest;
import com.example.productapi.entity.Cart;
import com.example.productapi.entity.CartItem;
import com.example.productapi.entity.Coupon;
import com.example.productapi.entity.Order;
import com.example.productapi.entity.OrderItem;
import com.example.productapi.entity.Product;
import com.example.productapi.entity.User;
import com.example.productapi.enums.DiscountType;
import com.example.productapi.enums.OrderStatus;
import com.example.productapi.enums.UserRole;
import com.example.productapi.exception.BadRequestException;
import com.example.productapi.exception.ForbiddenException;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.repository.CartRepository;
import com.example.productapi.repository.CouponRepository;
import com.example.productapi.repository.OrderRepository;
import com.example.productapi.repository.ProductRepository;
import com.example.productapi.repository.UserRepository;
import com.example.productapi.security.CurrentUserService;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final CouponRepository couponRepository;
	private final CurrentUserService currentUserService;

	public OrderService(OrderRepository orderRepository, CartRepository cartRepository, UserRepository userRepository,
			ProductRepository productRepository, CouponRepository couponRepository,
			CurrentUserService currentUserService) {
		this.orderRepository = orderRepository;
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.couponRepository = couponRepository;
		this.currentUserService = currentUserService;
	}

	@Transactional
	public OrderResponse createOrder(CreateOrderRequest request) {
		User user = currentUserService.getCurrentUser();

		Cart cart = cartRepository.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalArgumentException("Giỏ hàng đang trống"));

		if (cart.getItems() == null || cart.getItems().isEmpty()) {
			throw new IllegalArgumentException("Giỏ hàng đang trống");
		}

		Order order = new Order();
		order.setUser(user);
		order.setShippingAddress(request.getShippingAddress());
		order.setReceiverName(request.getReceiverName());
		order.setReceiverPhone(request.getReceiverPhone());

		BigDecimal totalAmount = BigDecimal.ZERO;

		for (CartItem cartItem : cart.getItems()) {
			Product product = cartItem.getProduct();

			if (product.getQuantity() < cartItem.getQuantity()) {
				throw new IllegalArgumentException("Sản phẩm " + product.getName() + " không đủ tồn kho");
			}

			BigDecimal itemTotal = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(product);
			orderItem.setProductName(product.getName());
			orderItem.setUnitPrice(cartItem.getUnitPrice());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setTotalPrice(itemTotal);

			order.getItems().add(orderItem);

			product.setQuantity(product.getQuantity() - cartItem.getQuantity());
			productRepository.save(product);

			totalAmount = totalAmount.add(itemTotal);
		}

		BigDecimal discountAmount = calculateDiscountAmount(request.getCouponCode(), totalAmount);

		BigDecimal finalAmount = totalAmount.subtract(discountAmount);

		order.setTotalAmount(totalAmount);
		order.setDiscountAmount(discountAmount);
		order.setFinalAmount(finalAmount);

		if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
			String couponCode = request.getCouponCode().trim().toUpperCase();

			Coupon coupon = couponRepository.findByCode(couponCode)
					.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã giảm giá: " + couponCode));

			coupon.setUsedCount(coupon.getUsedCount() + 1);
			couponRepository.save(coupon);

			order.setCouponCode(couponCode);
		}

		Order savedOrder = orderRepository.save(order);

		cart.getItems().clear();
		cartRepository.save(cart);

		return mapToResponse(savedOrder);
	}

	@Transactional
	public OrderResponse cancelOrder(Long orderId) {
		User currentUser = currentUserService.getCurrentUser();

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id = " + orderId));

		boolean isOwner = order.getUser().getId().equals(currentUser.getId());
		boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;
		boolean isSeller = currentUser.getRole() == UserRole.SELLER;

		if (!isOwner && !isAdmin && !isSeller) {
			throw new ForbiddenException("Bạn không có quyền hủy đơn hàng này");
		}

		if (order.getStatus() == OrderStatus.CANCELLED) {
			throw new IllegalArgumentException("Đơn hàng này đã bị hủy trước đó");
		}

		if (order.getStatus() != OrderStatus.PENDING) {
			throw new IllegalArgumentException("Chỉ được hủy đơn hàng đang ở trạng thái PENDING");
		}

		for (OrderItem item : order.getItems()) {
			Product product = item.getProduct();
			product.setQuantity(product.getQuantity() + item.getQuantity());
			productRepository.save(product);
		}

		order.setStatus(OrderStatus.CANCELLED);

		Order savedOrder = orderRepository.save(order);

		return mapToResponse(savedOrder);
	}

	public List<OrderResponse> getOrdersByUserId(Long userId) {
		userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với id = " + userId));

		return orderRepository.findByUserId(userId).stream().map(this::mapToResponse).toList();
	}

	public OrderResponse getOrderById(Long orderId) {
		User currentUser = currentUserService.getCurrentUser();

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id = " + orderId));

		boolean isOwner = order.getUser().getId().equals(currentUser.getId());
		boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

		if (!isOwner && !isAdmin) {
			throw new ForbiddenException("Bạn không có quyền xem đơn hàng này");
		}

		return mapToResponse(order);
	}

	@Transactional
	public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
		User currentUser = currentUserService.getCurrentUser();

		boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;
		boolean isSeller = currentUser.getRole() == UserRole.SELLER;

		if (!isAdmin && !isSeller) {
			throw new ForbiddenException("Bạn không có quyền cập nhật trạng thái đơn hàng");
		}

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id = " + orderId));

		validateOrderStatusTransition(order.getStatus(), request.getStatus());

		order.setStatus(request.getStatus());

		Order savedOrder = orderRepository.save(order);

		return mapToResponse(savedOrder);
	}

	public List<OrderResponse> getMyOrders() {
		Long userId = currentUserService.getCurrentUserId();

		return orderRepository.findByUserId(userId).stream().map(this::mapToResponse).toList();
	}

	private Order findOrderById(Long orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id = " + orderId));
	}

	private void validateOrderStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
		if (currentStatus == null || newStatus == null) {
			throw new BadRequestException("Trạng thái đơn hàng không hợp lệ");
		}

		if (currentStatus == newStatus) {
			throw new BadRequestException("Đơn hàng đã ở trạng thái " + currentStatus);
		}

		if (currentStatus == OrderStatus.PENDING) {
			if (newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELLED) {
				return;
			}

			throw new BadRequestException("Đơn hàng PENDING chỉ được chuyển sang CONFIRMED hoặc CANCELLED");
		}

		if (currentStatus == OrderStatus.CONFIRMED) {
			if (newStatus == OrderStatus.SHIPPING || newStatus == OrderStatus.CANCELLED) {
				return;
			}

			throw new BadRequestException("Đơn hàng CONFIRMED chỉ được chuyển sang SHIPPING hoặc CANCELLED");
		}

		if (currentStatus == OrderStatus.SHIPPING) {
			if (newStatus == OrderStatus.COMPLETED) {
				return;
			}

			throw new BadRequestException("Đơn hàng SHIPPING chỉ được chuyển sang COMPLETED");
		}

		if (currentStatus == OrderStatus.COMPLETED) {
			throw new BadRequestException("Đơn hàng COMPLETED là trạng thái cuối, không thể thay đổi");
		}

		if (currentStatus == OrderStatus.CANCELLED) {
			throw new BadRequestException("Đơn hàng CANCELLED là trạng thái cuối, không thể thay đổi");
		}
	}

	private BigDecimal calculateDiscountAmount(String couponCode, BigDecimal totalAmount) {
		if (couponCode == null || couponCode.isBlank()) {
			return BigDecimal.ZERO;
		}

		String normalizedCode = couponCode.trim().toUpperCase();

		Coupon coupon = couponRepository.findByCode(normalizedCode)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã giảm giá: " + normalizedCode));

		if (!Boolean.TRUE.equals(coupon.getActive())) {
			throw new IllegalArgumentException("Mã giảm giá không hoạt động");
		}

		if (coupon.getUsedCount() >= coupon.getUsageLimit()) {
			throw new IllegalArgumentException("Mã giảm giá đã hết lượt sử dụng");
		}

		LocalDateTime now = LocalDateTime.now();

		if (coupon.getStartDate() != null && now.isBefore(coupon.getStartDate())) {
			throw new IllegalArgumentException("Mã giảm giá chưa đến thời gian sử dụng");
		}

		if (coupon.getEndDate() != null && now.isAfter(coupon.getEndDate())) {
			throw new IllegalArgumentException("Mã giảm giá đã hết hạn");
		}

		if (coupon.getMinOrderAmount() != null && totalAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
			throw new IllegalArgumentException("Đơn hàng chưa đạt giá trị tối thiểu để dùng mã giảm giá");
		}

		BigDecimal discountAmount;

		if (coupon.getDiscountType() == DiscountType.PERCENT) {
			discountAmount = totalAmount.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));
		} else {
			discountAmount = coupon.getDiscountValue();
		}

		if (coupon.getMaxDiscountAmount() != null && discountAmount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
			discountAmount = coupon.getMaxDiscountAmount();
		}

		if (discountAmount.compareTo(totalAmount) > 0) {
			discountAmount = totalAmount;
		}

		return discountAmount;
	}

	private OrderResponse mapToResponse(Order order) {
		List<OrderItemResponse> itemResponses = order.getItems().stream().map(this::mapItemToResponse).toList();

		return new OrderResponse(order.getId(), order.getUser().getId(), order.getUser().getEmail(),
				order.getTotalAmount(), order.getCouponCode(), order.getDiscountAmount(), order.getFinalAmount(),
				order.getStatus(), order.getShippingAddress(), order.getReceiverName(), order.getReceiverPhone(),
				order.getCreatedAt(), order.getUpdatedAt(), itemResponses);
	}

	private OrderItemResponse mapItemToResponse(OrderItem item) {
		return new OrderItemResponse(item.getId(), item.getProduct().getId(), item.getProductName(),
				item.getUnitPrice(), item.getQuantity(), item.getTotalPrice());
	}
}