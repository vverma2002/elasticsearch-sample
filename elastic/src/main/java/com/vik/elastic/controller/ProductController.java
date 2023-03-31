package com.vik.elastic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vik.elastic.modal.Product;
import com.vik.elastic.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public ResponseEntity<Page<Product>> getproducts() {
		Pageable page = Pageable.ofSize(5);
		return ResponseEntity.ok(productService.getAll(page));
	}

	@PostMapping
	public ResponseEntity<Product> saveProduct(Product product) {
		return ResponseEntity.ok(productService.createProductIndex(product));
	}
}