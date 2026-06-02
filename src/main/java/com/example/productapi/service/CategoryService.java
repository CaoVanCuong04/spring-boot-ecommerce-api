package com.example.productapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.productapi.dto.CategoryRequest;
import com.example.productapi.dto.CategoryResponse;
import com.example.productapi.entity.Category;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.repository.CategoryRepository;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<CategoryResponse> getAllCategories() {
		return categoryRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	public CategoryResponse getCategoryById(Long id) {
		Category category = findCategoryById(id);
		return mapToResponse(category);
	}

	public CategoryResponse createCategory(CategoryRequest request) {
		if (categoryRepository.existsByName(request.getName())) {
			throw new IllegalArgumentException("Tên danh mục đã tồn tại");
		}

		Category category = new Category();
		category.setName(request.getName());
		category.setDescription(request.getDescription());
		category.setActive(request.getActive() == null ? true : request.getActive());

		Category savedCategory = categoryRepository.save(category);

		return mapToResponse(savedCategory);
	}

	public CategoryResponse updateCategory(Long id, CategoryRequest request) {
		Category category = findCategoryById(id);

		category.setName(request.getName());
		category.setDescription(request.getDescription());
		category.setActive(request.getActive() == null ? category.getActive() : request.getActive());

		Category updatedCategory = categoryRepository.save(category);

		return mapToResponse(updatedCategory);
	}

	public void deleteCategory(Long id) {
		Category category = findCategoryById(id);
		categoryRepository.delete(category);
	}

	private Category findCategoryById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id = " + id));
	}

	private CategoryResponse mapToResponse(Category category) {
		return new CategoryResponse(category.getId(), category.getName(), category.getDescription(),
				category.getActive(), category.getCreatedAt(), category.getUpdatedAt());
	}
}