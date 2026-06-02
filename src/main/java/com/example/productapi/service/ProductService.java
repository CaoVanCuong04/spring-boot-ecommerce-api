package com.example.productapi.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.productapi.dto.PageResponse;
import com.example.productapi.dto.ProductRequest;
import com.example.productapi.dto.ProductResponse;
import com.example.productapi.entity.Category;
import com.example.productapi.entity.Product;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.repository.CategoryRepository;
import com.example.productapi.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	public List<ProductResponse> getAllProducts() {
		return productRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	public ProductResponse getProductById(Long id) {
		Product product = findProductById(id);
		return mapToResponse(product);
	}

	public ProductResponse createProduct(ProductRequest request) {
		Category category = findCategoryById(request.getCategoryId());

		Product product = new Product();
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setQuantity(request.getQuantity());
		product.setImageUrl(request.getImageUrl());
		product.setActive(request.getActive() == null ? true : request.getActive());
		product.setCategory(category);

		Product savedProduct = productRepository.save(product);

		return mapToResponse(savedProduct);
	}

	public ProductResponse updateProduct(Long id, ProductRequest request) {
		Product product = findProductById(id);
		Category category = findCategoryById(request.getCategoryId());

		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setQuantity(request.getQuantity());
		product.setImageUrl(request.getImageUrl());
		product.setActive(request.getActive() == null ? product.getActive() : request.getActive());
		product.setCategory(category);

		Product updatedProduct = productRepository.save(product);

		return mapToResponse(updatedProduct);
	}

	public void deleteProduct(Long id) {
		Product product = findProductById(id);
		productRepository.delete(product);
	}

	public List<ProductResponse> getProductsByCategory(Long categoryId) {
		findCategoryById(categoryId);

		return productRepository.findByCategoryId(categoryId).stream().map(this::mapToResponse).toList();
	}

	public List<ProductResponse> searchProducts(String keyword) {
		return productRepository.findByNameContainingIgnoreCase(keyword).stream().map(this::mapToResponse).toList();
	}

	private Product findProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id = " + id));
	}

	private Category findCategoryById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id = " + id));
	}

	private ProductResponse mapToResponse(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
				product.getQuantity(), product.getImageUrl(), product.getActive(), product.getCategory().getId(),
				product.getCategory().getName(), product.getCreatedAt(), product.getUpdatedAt());
	}

	public PageResponse<ProductResponse> getProductsWithFilter(int page, int size, String keyword, Long categoryId,
			BigDecimal minPrice, BigDecimal maxPrice, Boolean active, String sortBy, String sortDir) {
		if (page < 0) {
			page = 0;
		}

		if (size <= 0) {
			size = 10;
		}

		if (size > 50) {
			size = 50;
		}

		String sortField = validateSortField(sortBy);

		Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		String normalizedKeyword = null;
		if (keyword != null && !keyword.trim().isEmpty()) {
			normalizedKeyword = keyword.trim();
		}

		Page<Product> productPage = productRepository.filterProducts(normalizedKeyword, categoryId, minPrice, maxPrice,
				active, pageable);

		List<ProductResponse> content = productPage.getContent().stream().map(this::mapToResponse).toList();

		return new PageResponse<>(content, productPage.getNumber(), productPage.getSize(),
				productPage.getTotalElements(), productPage.getTotalPages(), productPage.isLast());
	}

	private String validateSortField(String sortBy) {
		if (sortBy == null || sortBy.trim().isEmpty()) {
			return "id";
		}

		return switch (sortBy) {
		case "id", "name", "price", "quantity" -> sortBy;
		default -> "id";
		};
	}
}