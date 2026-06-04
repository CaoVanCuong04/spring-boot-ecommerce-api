package com.example.productapi.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.productapi.exception.BadRequestException;

@Service
public class CloudinaryService {

	private final Cloudinary cloudinary;

	public CloudinaryService(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}

	public String uploadProductImage(MultipartFile file) {
		validateImageFile(file);

		try {
			Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
					ObjectUtils.asMap("folder", "ecommerce/products", "resource_type", "image"));

			return uploadResult.get("secure_url").toString();

		} catch (Exception e) {
			e.printStackTrace();
			throw new BadRequestException("Upload ảnh thất bại: " + e.getMessage());
		}
	}

	private void validateImageFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new BadRequestException("File ảnh không được để trống");
		}

		String contentType = file.getContentType();

		if (contentType == null || !contentType.startsWith("image/")) {
			throw new BadRequestException("File upload phải là ảnh");
		}

		long maxSize = 5 * 1024 * 1024;

		if (file.getSize() > maxSize) {
			throw new BadRequestException("Dung lượng ảnh không được vượt quá 5MB");
		}
	}
}