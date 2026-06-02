package com.example.productapi.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.productapi.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByCategoryId(Long categoryId);

	List<Product> findByNameContainingIgnoreCase(String keyword);

	@Query("""
			SELECT p FROM Product p
			WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
			       OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
			  AND (:categoryId IS NULL OR p.category.id = :categoryId)
			  AND (:minPrice IS NULL OR p.price >= :minPrice)
			  AND (:maxPrice IS NULL OR p.price <= :maxPrice)
			  AND (:active IS NULL OR p.active = :active)
			""")
	Page<Product> filterProducts(@Param("keyword") String keyword, @Param("categoryId") Long categoryId,
			@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice,
			@Param("active") Boolean active, Pageable pageable);
}